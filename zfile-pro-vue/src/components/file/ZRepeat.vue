<template>
	<div class="zfile-file-repeat-body">
		<el-dialog v-if="repeatVisible" v-model="repeatVisible" :destroy-on-close="true"
		           @close="closeDialog"
				   @opened="openDialog"
				   :z-index=101
		           :title="getTitle"
		           draggable
		           top="5vh"
		           width="70%">
			<el-collapse v-if="repeatFileList.length>0" v-model="activeNames" @change="handleChange">
				<el-checkbox-group v-model="repeatDeleteList">
					<el-collapse-item v-for="(rowItem, index) in repeatFileList" :key="index+'zfile-repeat'" :title="'第' + (index+1)+'组'" :name="index">
						<el-row :gutter="20">
							<el-col class="zfile-img-col" :span="6"
								:key="indexTtem +'zfile-img-col'"
								v-for="(colItem,indexTtem) in rowItem">
								<div v-if="colItem?.url">
									<el-image v-if="common.getFileType(colItem.name)=='image'" key="md5(colItem.url)" loading="lazy" lazy
										class=""
										:src="colItem.thumbnail" fit="cover" 
										:preview-src-list="[colItem.url]"
										:alt="colItem.name"/>
									<div>
										<el-checkbox :key="md5(colItem.url)+'-checkbox'" :label="colItem" :title="colItem.name">
										{{ colItem.name }}
										</el-checkbox>
										<p class="">{{'/'+storageKey+colItem.path}}</p>
										<p class="">{{common.fileSizeFormat(colItem.size)}}</p>
										<el-button type="info" plain size="small" @click="openRow(colItem)">下载</el-button>
									</div>
								</div>
							</el-col>
						</el-row>
					</el-collapse-item>
				</el-checkbox-group>
			</el-collapse>
			<el-empty description="无重复文件" v-if="repeatFileList.length<1"/>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="closeDialog">关闭</el-button>
					<el-button type="primary" @click="batchDelete" :disabled="repeatDeleteList.length<1" >
					删除选中
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
import md5 from "md5";

import common from "~/common";

let router = useRouter();
let route = useRoute();

import useFileRepeat from "~/composables/file/useFileRepeat";
const { repeatVisible, repeatFileList, findRepeatFile, repeatDeleteList, batchDelete, openRow } = useFileRepeat();

import useStorageConfigStore from "~/stores/storage-config";
let storageConfigStore = useStorageConfigStore();

import useFileDataStore from "~/stores/file-data";
let fileDataStore = useFileDataStore();

import useGlobalConfigStore from "~/stores/global-config";
let globalConfigStore = useGlobalConfigStore();

import useRouterData from "~/composables/useRouterData";
let { storageKey } = useRouterData();

// 折叠面板变化
const handleChange = (val) => {

}

// 获取对话框标题
const getTitle = computed(() => { 
	let path = !!route.params.fullpath && route.params.fullpath.length>0 ? '/'+route.params.fullpath.join('/') : '';
	return '查找重复文件[/'+route.params.storageKey + path+']';
});
// 默认全部打开折叠面板
const activeNames = computed(() => { 
	let arr = []
	for(let i=0; i<repeatFileList.value.length; i++){
		arr.push(i);
	}
	return arr;
});

// 如果有上传完成的文件，关闭对话框时调用 close 方法刷新文件列表
const emit = defineEmits()
const closeDialog = () => {
	repeatVisible.value = false;
	emit('close');
}
// 初始化操作
const openDialog = () => {
	console.log("zrepeat openDialog");
	repeatDeleteList.value = [];
	repeatFileList.value = [];
	findRepeatFile();
}
// 挂载事件
onMounted(() => {
	
})
// 销毁事件
onUnmounted(() => {
	console.log("zrepeat unmounted");
})

// 监听对话框关闭和打开
watch(() => repeatVisible.value, (value) => {
	repeatDeleteList.value = [];
	repeatFileList.value = [];
})

</script>

<style scoped lang="scss">

.zfile-file-repeat-body {
	:deep(.el-dialog__header) {
		text-align: center;
	}

	:deep(.el-dialog__body) {
		max-height: 80vh;
		overflow-y: auto;
	}

	.el-row {
		margin-bottom: 20px;
	}
	.el-row:last-child {
		margin-bottom: 0;
	}
	:deep(.el-checkbox) {
		width: 100%;
		display: block;
		position: relative;
		height: 24px;
		.el-checkbox__input{
			border: solid 1px blue;
		}
		.el-checkbox__label {
			display: inline-block;
			width: calc(100% - 20px);
			text-overflow: ellipsis; 
			white-space: nowrap;
			overflow: hidden;
		}
		
	}
}

</style>