<template>
	<z-form :model="authList"
	        class="zfile-admin-password-form"
	        v-loading="loading">
		<template #form-title>
			<div class="flex justify-items-center">
				<router-link to="/admin/storage-list">
					<svg-icon class="inline mr-2 cursor-pointer" name="file-type-back"></svg-icon>
				</router-link>
				<span>授权管理（{{storageItem?.name}}）</span>
			</div>
		</template>
		<template #form-sub-title>
			<el-alert :closable="false" type="info">
				<div class="rules-tips">可以对存储源进行读和写授权，写包含读权限</div>
			</el-alert>
		</template>

		<z-form-item v-for="(item, index) in authList"
		             :required="true"
		             :key="item"
		             class="expression-item">
			<div class="sm:flex sm:space-x-2 sm:border-b-0 sm:pb-0 border-b pb-2">
				<el-select size="default" v-model="item.userId" placeholder="请选择用户">
					<el-option v-for="item in userList"
						:disabled="item.disabled"
						:key="item.id"
						:label="item.username+'('+item.usernameZh+')'"
						:value="item.id">
					</el-option>
				</el-select>
				<el-select size="default" v-model="item.permission" placeholder="请选择权限">
					<el-option v-for="item in permissionList"
						:key="item"
						:label="item"
						:value="item">
					</el-option>
				</el-select>
				<el-button type="danger" @click="deleteAuthItem(index)" :icon="Delete"></el-button>
			</div>
		</z-form-item>

		<z-form-item>
			<el-button type="primary" size="default" :icon="Plus" @click="addAuthItem">添加更多</el-button>
		</z-form-item>

		<template #footer>
			<el-button type="primary" size="default" :icon="CheckBadgeIcon" @click="saveAuthData">保存设置</el-button>
		</template>
	</z-form>
</template>

<script setup>
import {Plus, Delete, Collection, Notebook} from '@element-plus/icons-vue'
import {CheckBadgeIcon} from '@heroicons/vue/24/solid'
import {KeyIcon, FolderIcon} from '@heroicons/vue/24/outline'
import ZForm from "/src/components/form/ZForm.vue";
import ZFormItem from "/src/components/form/ZFormItem.vue";

let route = useRoute();
let router = useRouter();
let currentStorageId = route.params.storageId;

import useStorageAuth from "~/composables/admin/storage/storage-auth.js";
import { loadStorageItemReq } from "~/api/admin-storage";
import { loadUserListReq } from "~/api/admin-user";
const { loading, loadAuthData, authList,
		addAuthItem, deleteAuthItem,
		saveAuthData } = useStorageAuth(router, route);

const  permissionList = ['读', '写']
let userList = ref();

onMounted(() => {
	loadAuthData();
	loadStorageItem();
	loadUserList();
})

const storageItem = ref();
// 加载指定存储源的数据
const loadStorageItem = () => {
  loadStorageItemReq(currentStorageId).then((res) => {
    res.data.type = res.data.type.key;
    storageItem.value = res.data;
  })
}
// 加载用户列表
const loadUserList = () => {
	loadUserListReq().then((res) => {
		userList.value = res.data;
	})
}


</script>

<style lang="scss" scoped>
.expression-item {
	:deep(.el-input__wrapper) {
		@apply w-full sm:w-36 md:w-48 lg:w-64 xl:w-80
	}
}

.zfile-admin-password-form {
	:deep(.z-form-sub-title) {
		max-width: 100%;
	}

	.rules-tips {
		@apply text-sm font-bold p-1;
	}

	.rules-tips-link {
		@apply space-x-5 mt-2;
		svg {
			height: 1rem;
			line-height: 1.25rem;
			vertical-align: text-bottom;
		}
	}
}
</style>


<route lang="yaml">
meta:
  layout: admin
  name: 存储源授权管理
</route>