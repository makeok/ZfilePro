<template>
	<div class="zfile-file-upload-body">
		<el-dialog v-if="copyMoveVisible" v-model="copyMoveVisible" :destroy-on-close="true"
		           @close="closeDialog"
				   @open="openDialog"
		           :title="getTitle"
		           draggable
		           top="5vh"
		           width="70%">
			<el-text class="">请选择目标文件夹:</el-text>
			<el-tree :data="treeData" :props="treeDefaultProps" :expand-on-click-node="true" node-key="url" 
				:load="loadNode" lazy @node-click="handleNodeClick" >
				<template #default="{ node, data }">
					<span class="custom-tree-node">
						<span v-if="data.nodePath==targetPath" style="color: blue;">{{ data.name }}</span>
						<span v-else>{{ data.name }}</span>
					</span>
				</template>
			</el-tree>

			<div class="mt-5 space-y-2.5">
				<div class="flex flex-row w-full relative rounded-lg" v-for="item in copyMoveResultList" :key="item.nodePath">
					<div class="p-1.5 py-2.5 sm:py-3 w-full flex flex-col justify-between">
						<div class="flex justify-between">
							<div class="flex sm:space-x-5 flex-col sm:flex-row">
								<div class="font-medium text-sm line-clamp-1">{{ item.nodePath }}</div>
								<div class="text-gray-400 text-xs leading-5 line-clamp-1 active:line-clamp-none">
									<span v-if="item.status === 'doing' && !item.msg" class="text-blue-500 box animate__animated animate__fadeIn">{{ item.speed }}</span>
									<span v-if="item.status === 'doing' && item.msg" class="text-blue-500 box animate__animated animate__fadeIn">{{ item.msg }}</span>
									<svg-icon v-else-if="item.status === 'finished'" name="check" class="inline text-green-500 box animate__animated animate__fadeIn"/>
									<span v-else-if="item.status === 'waiting'" class="text-yellow-500 box animate__animated animate__fadeIn">{{ item.msg }}</span>
									<span v-else-if="item.status === 'error'" class="text-red-500 box animate__animated animate__fadeIn">{{ item.msg }}</span>
								</div>
							</div>
							<div>
								<div v-if="item.status === 'doing'">
									<el-icon class="top-0.5 relative inline text-gray-500 mr-1 text-lg cursor-pointer rounded-full hover:bg-gray-200 box animate__animated animate__fadeIn">
										<Loading />
									</el-icon>
								</div>
								<div v-else-if="item.status === 'finished'">
								<svg-icon @click="removeResultItemByIndex(item.index)" name="delete" class="inline text-red-400 mr-1 text-base cursor-pointer rounded-full hover:bg-gray-200 box animate__animated animate__fadeIn"/>
								</div>
								<div v-else-if="item.status === 'error'">
								<svg-icon @click="retryCopy(item)" name="refresh" class="inline text-red-500 mr-1 text-base cursor-pointer rounded-full hover:bg-gray-200 box animate__animated animate__fadeIn"/>
								</div>
							</div>
						</div>
						<div>
							<el-progress v-if="item.status === 'finished'" :show-text="false" :percentage="item.progress" status="success"></el-progress>
							<el-progress v-else-if="item.status === 'doing'" :show-text="false" :percentage="item.progress"></el-progress>
							<el-progress v-else-if="item.status === 'error'" :show-text="false" :percentage="100" status="exception"></el-progress>
							<el-progress v-else-if="item.status === 'waiting'" :show-text="false" :percentage="0"></el-progress>
						</div>
					</div>
				</div>
			</div>
			<template #footer>
				<span class="dialog-footer">
					<el-text class="">是否覆盖:</el-text><el-switch v-model="overwrite" class="mr-3 ml-3" />
					<el-text class="">目标文件夹:</el-text><el-text style="margin: 0 1em;;" type="danger">{{ targetPath }}</el-text>
					<el-button @click="closeDialog">关闭</el-button>
					<el-button type="primary" @click="doConfirm" :disabled="!!!targetPath" >
					确定
					</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup>
import {
  Loading
} from '@element-plus/icons-vue';

import common from "~/common";

let router = useRouter();
let route = useRoute();

import useFileCopyMove from "~/composables/file/useFileCopyMove";
const { copyMoveVisible, copyMoveMode, isFile } = useFileCopyMove();

import useStorageConfigStore from "~/stores/storage-config";
let storageConfigStore = useStorageConfigStore();

import useFileDataStore from "~/stores/file-data";
let fileDataStore = useFileDataStore();

import useFileSelect from "~/composables/file/useFileSelect";
let { selectRows, selectRow, selectFolders, selectFiles } = useFileSelect();

import {loadFileListReq, loadStorageConfigReq} from "~/api/home";
import {copyFileReq, copyFolderReq, moveFileReq, moveFolderReq} from "~/api/file-operator";

import useFilePwd from "~/composables/file/useFilePwd";
let { getPathPwd, putPathPwd } = useFilePwd();
import useRouterData from "~/composables/useRouterData";
let { storageKey } = useRouterData();

const loading = ref(false);
const overwrite = ref(false);
let copyMoveResultList = ref([]);

// 获取对话框标题
const getTitle = computed(() => { 
	let first = copyMoveMode.value === 'copy' ? '复制' : '移动'
	let second = isFile.value ? '文件' : '文件夹'
	return first+second;
});
// 加载数据
const loadFile = (resolve, currentPath) => {
	// 未指定 storageKey 时, 不执行任何操作.
	if (!storageKey.value) {
		return;
	}
	loading.value = true;

	let param = {};
	param.storageKey = storageKey.value;
	param.path = currentPath;
	param.password = param.password || getPathPwd();
	param.orderBy = storageConfigStore.globalConfig.defaultSortField;
	param.orderDirection = storageConfigStore.globalConfig.defaultSortOrder;

	let requestStorageId = storageKey.value;
	loadFileListReq(param).then((response) => {

		let passwordPattern = response.data.passwordPattern;

		// 如果请求的 storageKey 和当前的 storageKey 不一致
		// 则表示再加载数据期间，修改了 storageKey, 为了防止数据错乱, 取消本次渲染.
		if (requestStorageId !== storageKey.value) {
			return;
		}
		let folder = [];
		let fileList = response.data.files;
		if(fileList == null || fileList.length < 1) {
			resolve(folder);
			return;
		}
		
		for(let k=0; k<fileList.length; k++) {
			let nodePath = fileList[k].path==="/"? fileList[k].path + fileList[k].name : fileList[k].path + '/' + fileList[k].name;
			fileList[k].nodePath = nodePath;
			if(fileList[k].type === 'FOLDER'){
				folder.push(fileList[k]);
			}else{
				fileList[k].disabled = true;
				fileList[k].isLeaf = true;
			}
		}
		resolve(folder);

	}).catch((error) => {
		let data = error.data;
		// 如果需要密码或密码错误进行提示, 并弹出输入密码的框.
		if (data.code === common.responseCode.INVALID_PASSWORD) {
			ElMessage.warning('密码错误，请重新输入！');
			popPassword();
		} else if (data.code === common.responseCode.REQUIRED_PASSWORD) {
			popPassword();
		} else {
			ElMessage.error(data.msg);
		}
	}).finally(() => {
		loading.value = false;
	});
}

// 如果有上传完成的文件，关闭对话框时调用 close 方法刷新文件列表
const emit = defineEmits()
const closeDialog = () => {
	copyMoveVisible.value = false;
	emit('close');
}
// 初始化操作
const openDialog = () => {
	
}
// 挂载事件
onMounted(() => {
	
})
// 销毁事件
onUnmounted(() => {
	console.log("zcopy unmounted");
})

// 监听对话框关闭和打开
watch(() => copyMoveVisible.value, (value) => {
	overwrite.value = false;
	copyMoveResultList.value = [];
	targetPath.value = ''
})

// 复制文件
const copyMoveOne = async (data, index, apiReq) => {
	let param = {};
	param.storageKey = storageKey.value;
	param.path = data.path;
	param.password = getPathPwd();
	param.name = data.name;
	param.targetPath = targetPath.value;
	param.targetName = data.name;
	param.overwrite = overwrite.value;
	param.targetPassword = getPathPwd(targetPath.value);
	
	// 添加响应结果
	let nodePath = data.path==="/"? data.path + data.name : data.path + '/' + data.name;
	let copyFileResultInfo = {
		name: data.name,
		path: data.path,
		progress: 30,
		status: 'doing',
		nodePath: nodePath,
		index: index
	}
	copyMoveResultList.value.push(copyFileResultInfo);

	await apiReq(param).then((response) => {
		nextTick(() => {
			if(response.code == 0) {
				copyMoveResultList.value[index].status = 'finished';
				copyMoveResultList.value[index].progress = 100;
			}else{
				copyMoveResultList.value[index].status = 'error';
				copyMoveResultList.value[index].msg = response.msg;
			}
		})
		
	}).catch((error) => {
		let data = error.data;
		// 如果需要密码或密码错误进行提示, 并弹出输入密码的框.
		if (data.code === common.responseCode.INVALID_PASSWORD) {
			ElMessage.warning('密码错误，请重新输入！');
			popPassword();
		} else if (data.code === common.responseCode.REQUIRED_PASSWORD) {
			popPassword();
		} else {
			// ElMessage.error(data.msg);
			copyMoveResultList.value[index].status = 'error';
			copyMoveResultList.value[index].msg = data.msg;
		}
	}).finally(() => {

	});
}

// 文件复制
const copyMoveAll = async () => {
	// 清空结果列表
	copyMoveResultList.value = [];
	loading.value = true;
	// 打开全屏 loading
	const loadingInstance = ElLoading.service({
		text: '文件读取中...',
		background: 'rgba(0, 0, 0, .3)'
	})
	for(let k=0; k<selectRows.value.length; k++) {
		let data = selectRows.value[k];
		let apiReq = null;
		if(copyMoveMode.value === 'copy') {
			if(isFile.value) {
				apiReq = copyFileReq;
			}else{
				apiReq = copyFolderReq;
			}
		}else{
			if(isFile.value) {
				apiReq = moveFileReq;
			}else{
				apiReq = moveFolderReq;
			}
		}
		await copyMoveOne(data, k, apiReq);
	}
	loadingInstance.close();
	loading.value = false;
}

// 确定按钮事件
const doConfirm = () => {
	copyMoveAll();
}

// 移除文件的结果
const removeResultItemByIndex = (fileIndex) => {
	if (fileIndex === null || fileIndex === undefined) {
		return;
	}

	let removeIndex = copyMoveResultList.value.findIndex((item, index) => {
		if (item.index === fileIndex) {
			copyMoveResultList.value.splice(index, 1);
			return true;
		}
	})
}

const retryCopy = (item) => {
	removeResultItemByIndex(item.index);
	console.log('重新上传文件', item);
	removeResultItemByIndex(item.index);
	let data = {
		name: item.name,
		path: item.path,
		nodePath: item.nodePath,
	}
	copyFile(data, item.index);
}


// 文件树
let targetPath = ref('');
const handleNodeClick = (data) => {
	let nodePath = data.path==="/"? data.path + data.name : data.path + '/' + data.name;
	if(data.type === 'FOLDER'){
		targetPath.value = nodePath;
	}
}
const loadNode = (node, resolve) => {
	let data = node.data;
	if(data.nodePath == null) {
		data.nodePath = "/";
	}
	loadFile(resolve, data.nodePath);
	// return resolve([])
}
const customNodeClass = (data, node) => {
	if (data.disabled) {
		return 'tree-node-is-disabled'
	}
	if(data.nodePath == targetPath.value) {
		return 'tree-node-is-selected';
	}
	return null
}
const treeData = [

]

const treeDefaultProps = {
  children: 'children',
  label: 'name',
  disabled: 'disabled',
  class: customNodeClass,
  isLeaf: 'isLeaf',
}
</script>

<style scoped lang="scss">

.zfile-file-upload-body {
	:deep(.el-dialog__header) {
		text-align: center;
	}

	:deep(.el-dialog__body) {
		max-height: 80vh;
		overflow-y: auto;
	}

  :deep(.el-upload-dragger) {
    @apply border-dashed border-2;
  }

	.drop-view {
		@apply fixed w-full h-full z-10
		bg-opacity-20 bg-black
		left-0 bottom-0
		flex justify-center items-center flex-row;

		.drop-sub {
			@apply flex justify-center items-center h-5/6 w-5/6
			border-dashed border-2 border-gray-400 rounded-2xl
			text-gray-500 font-bold text-2xl;
		}
	}

	.tree-node-is-disabled {
		background: 'lightgray';
		@apply text-gray-500;
	}
	.tree-node-is-selected  > :deep(.el-tree-node__content){
		color: #626aef;
		@apply text-gray-500;
	}
}

</style>