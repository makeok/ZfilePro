<template>
	<div class="zfile-admin-down-link">
		<el-card>
			<div class="flex justify-between">
				<h3 class="text-lg leading-6 font-medium text-gray-900">
					下载日志
				</h3>
				<div v-if="data" class="flex space-x-1.5 justify-center items-center cursor-pointer">
					<span class="text-gray-400 text-sm font-bold">记录下载日志：</span>
					<el-switch @change="saveData"
					           active-text="是"
					           inline-prompt
					           inactive-text="否"
					           v-model="data.recordDownloadLog"></el-switch>
				</div>
			</div>

			<div class="mt-4">
				<el-form inline v-model="searchParam">
					<el-form-item label="存储源">
						<el-select clearable :teleported="false" v-model="searchParam.storageKey" placeholder="请选择存储源">
							<el-option
								v-for="item in storageList"
								:key="item.id"
								:label="item.name"
								:value="item.key">
								<div class="flex justify-between space-x-20">
									<span>{{ item.name }}</span>
									<span class="text-gray-400">{{ item.type.description }}</span>
								</div>
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="短链 Key">
						<el-input v-model="searchParam.shortKey"></el-input>
					</el-form-item>
					<el-form-item label="文件路径">
						<el-input v-model="searchParam.path"></el-input>
					</el-form-item>
					<el-form-item label="请求 IP">
						<el-input v-model="searchParam.ip"></el-input>
					</el-form-item>
					<el-form-item label="UserAgent">
						<el-input v-model="searchParam.userAgent"></el-input>
					</el-form-item>
					<el-form-item label="Referer">
						<el-input v-model="searchParam.referer"></el-input>
					</el-form-item>
					<el-form-item label="下载时间">
						<el-date-picker
							v-model="searchParam.date"
							type="daterange"
							value-format="YYYY-MM-DD HH:mm:ss"
							:default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 2, 1, 23, 59, 59),]"
							range-separator="至"
							start-placeholder="开始时间"
							end-placeholder="结束时间"
						/>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" :icon="Search" @click="init">查询</el-button>
					</el-form-item>
				</el-form>

				<div class="mb-2">
					<el-button @click="batchDeleteLink" :icon="Delete" type="danger">批量删除选中</el-button>
          <el-button @click="batchDeleteLinkLogByQuery" :icon="Delete" type="danger">删除全部页</el-button>
				</div>

				<el-table border ref="linkTableRef" size="large" :data="pageData" style="width: 100%">
					<el-table-column type="selection" width="55" />
					<el-table-column fixed width="120" label="存储源名称">
						<template #default="scope">
							<el-tooltip
								:content="scope.row.storageType?.description"
								placement="right">
								<span>{{ scope.row.storageName }}</span>
							</el-tooltip>
						</template>
					</el-table-column>
					<el-table-column fixed width="90" align="center" label="下载类型">
						<template #default="scope">
							<el-tag v-if="scope.row.shortKey" type="success">短链</el-tag>
							<el-tag v-else>直链</el-tag>
						</template>
					</el-table-column>
					<el-table-column min-width="400" :show-overflow-tooltip="true" prop="shortKey" label="下载链接">
            <template #default="scope">
							<div class="space-x-2" v-if="scope.row.shortKey">
								<span>{{ generateShortLink(scope.row.shortKey) }}</span>
                <svg-icon @click="copyText(generateShortLink(scope.row.shortKey))" class="ml-2 inline cursor-pointer" name="copy"></svg-icon>
                <svg-icon @click="openLink(generateShortLink(scope.row.shortKey))" class="ml-1 inline cursor-pointer text-blue-500 text-sm" name="target"></svg-icon>
              </div>
							<div v-else>
								<span>{{ generateLink(scope.row) }}</span>
								<svg-icon @click="copyText(generateLink(scope.row))" class="ml-2 inline cursor-pointer" name="copy"></svg-icon>
								<svg-icon @click="openLink(generateLink(scope.row))" class="ml-1 inline cursor-pointer text-blue-500 text-sm" name="target"></svg-icon>
							</div>
            </template>
					</el-table-column>
					<el-table-column width="300" :show-overflow-tooltip="true" prop="path" label="文件路径">
					</el-table-column>
					<el-table-column width="100" :show-overflow-tooltip="true" prop="ip" label="请求 IP">
					</el-table-column>
					<el-table-column width="250" :show-overflow-tooltip="true" prop="userAgent" label="UserAgent">
					</el-table-column>
					<el-table-column width="200" :show-overflow-tooltip="true" prop="referer" label="Referer">
					</el-table-column>
					<el-table-column width="155" prop="createTime" label="下载时间">
					</el-table-column>
					<el-table-column fixed="right" width="120" label="操作">
						<template #default="scope">
							<el-popconfirm title="是否确认删除?" @confirm="deleteLink(scope.row.id)">
								<template #reference>
									<el-button :icon="Delete" type="danger">删除</el-button>
								</template>
							</el-popconfirm>
						</template>
					</el-table-column>
				</el-table>

				<el-config-provider :locale="zhCn">
					<el-pagination class="mt-3"
					               :page-size="searchParam.limit"
					               background
					               layout="total, sizes, prev, pager, next, jumper"
					               @current-change="handleCurrentChange"
					               @size-change="handleSizeChange"
					               v-model:current-page="searchParam.page"
					               :default-current-page="10"
					               :page-sizes="[10, 50, 100, 200, 1000, 99999999]"
					               :total="searchParam.total"/>
				</el-config-provider>
			</div>
		</el-card>
	</div>
</template>

<script setup>
import useLinkSetting from "~/composables/admin/link/useLinkSetting";
const { data, saveData, saveLoading } = useLinkSetting();

import {
  batchDeleteDownloadLogReq,
  batchDeleteDownloadLogByQueryReq,
  deleteDownloadLog,
  getDownloadLogList
} from "~/api/admin-download-link";
import zhCn from 'element-plus/es/locale/lang/zh-cn'


import { Search, Delete } from "@element-plus/icons-vue";
import {loadStorageListReq} from "~/api/admin-storage";
import { toClipboard } from "@soerenmartius/vue3-clipboard";
import { loadConfigReq } from "~/api/admin-setting";
import common from "~/common";

const searchParam = reactive({
	shortKey: '',
	storageKey: null,
	page: 1,
	limit: 10,
	path: '',
	ip: '',
	userAgent: '',
	referer: '',
	date: '',
	dateFrom: '',
	dateTo: '',
	total: 0,
});

const handleSizeChange = (val) => {
	searchParam.limit = val;
	searchParam.page = 1;
	init();
};

const handleCurrentChange = (val) => {
	searchParam.page = val;
	init();
};


const getSearchParam = () => {
  if (searchParam.date instanceof Array) {
    searchParam.dateFrom = searchParam.date[0];
    searchParam.dateTo = searchParam.date[1];
  } else {
    searchParam.dateFrom = '';
    searchParam.dateTo = '';
  }
  return searchParam;
}

const pageData = ref();

const init = () => {
	getDownloadLogList(getSearchParam()).then(res => {
		pageData.value = res.data;
		searchParam.total = res.dataCount;
	});
}

onMounted(() => {
	init();
	loadSourceList();
  loadSystemConfig();
})

const systemConfig = ref();
const loadSystemConfig = () => {
  loadConfigReq().then(res => {
    systemConfig.value = res.data;
  })
}

const storageList = ref();
const loadSourceList = () => {
	loadStorageListReq().then((response) => {
		storageList.value = response.data;
	});
}


const deleteLink = (id) => {
	deleteDownloadLog(id).then(res => {
		ElMessage.success('删除成功');
		init();
	});
}


const linkTableRef = ref();

const batchDeleteLink = () => {
	let selectionRows = linkTableRef.value.getSelectionRows();
	if (selectionRows.length === 0) {
		ElMessage.warning('请至少选择一条数据');
		return;
	}

	ElMessageBox.confirm('是否确认删除？', '提示', {
		type: 'warning'
	}).then(() => {
		let ids = selectionRows.map(item => item.id);
		batchDeleteDownloadLogReq({ids: ids}).then(res => {
			ElMessage.success('删除成功');
			init();
		});
	});
}


const batchDeleteLinkLogByQuery = () => {
  ElMessageBox.confirm(`是否确认删除当前查询条件下的 ${searchParam.total} 条直链下载日志？(如数量不对，请先点击查询后再用该功能按条件删除)`, '提示', {
    type: 'warning'
  }).then(() => {
    batchDeleteDownloadLogByQueryReq(getSearchParam()).then(res => {
      ElMessage.success('清空成功');
      init();
    });
  });
}

/**
 *  复制直链
 */
let copyText = (text) => {
  toClipboard(text).then(() => {
    ElMessage.success('复制成功');
  });
}


let openLink = (url) => {
	window.open(url);
}


let generateShortLink = (shortKey) => {
	return common.removeDuplicateSeparator(`${systemConfig?.value?.domain}/s/${shortKey}`);
}

let generateLink = (row) => {
	return common.removeDuplicateSeparator(`${systemConfig?.value?.domain}/${systemConfig?.value?.directLinkPrefix}/${row.storageKey}/${row.path}`);
}

</script>

<style scoped lang="scss">
.zfile-admin-down-link {
	:deep(.el-select-dropdown__item) {
		padding-right: 15px;
	}

	:deep(.el-dialog__header) {
		text-align: center;
	}

	:deep(.el-dialog__body) {
		height: 80vh;
		overflow-y: auto;
	}

}
</style>

<route lang="yaml">
meta:
  layout: admin
  name: 下载日志
</route>
