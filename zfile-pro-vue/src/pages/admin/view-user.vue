<template>
	<div>
		<el-card v-loading="loading">
			<div>
				<div class="flex justify-between">
					<h3 class="text-lg leading-6 font-medium text-gray-900">
						用户信息
					</h3>
				</div>
				<div class="my-2 text-sm text-gray-500 flex justify-between flex-wrap">
					<div>
						此页面显示用户相关的信息
					</div>
					<div>
						<el-input placeholder="请输入搜索内容" v-model="searchKey" :prefix-icon="Search"></el-input>
					</div>
				</div>
			</div>
			<ul role="list" class="user-container grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-4 lg:grid-cols-6">
				<li v-for="user in userListSearchResult" :key="user.id"
				    @dblclick="editUser(user)"
				    class="user-item group col-span-1 flex flex-col text-center bg-white rounded-lg border hover:shadow">
					<div class="flex-1 flex flex-col p-4 pb-4 relative">
						<svg-icon @click="deleteUser(user)" name="delete" class="absolute right-3 top-3 cursor-pointer group-hover:text-red-500"></svg-icon>
						<div class="rounded-full bg-blue-50 w-fit mx-auto">
							<img class="w-16 h-16 flex-shrink-0 mx-auto p-1" src="/src/assets/image/avator.png"/>
						</div>
						<h3 class="mt-1 text-gray-900 text-sm font-medium">{{ user.username }}</h3>
						<dl class="mt-1 flex-grow flex flex-col justify-between">
							<dd class="text-gray-500 text-sm line-clamp-1">
								{{ user.usernameZh }}
							</dd>
							<dd class="mt-1 space-x-1">
								<el-tag :type="user.role=='系统管理员'?'warning':''">{{user.role}}</el-tag>
								<el-tag type="success" @click="userSwitchEnableStatus(user)" v-show="user.enabled">启用</el-tag>
								<el-tag type="danger" @click="userSwitchEnableStatus(user)" v-show="!user.enabled">停用</el-tag>
							</dd>
						</dl>
					</div>
					<div class="-mt-px border-t flex divide-x divide-gray-200">
						<div @click="editUser(user)" class="w-0 flex-1 flex cursor-pointer">
						<span class="hover:text-blue-500 relative -mr-px w-0 flex-1 inline-flex items-center justify-center py-2 text-sm text-gray-700 font-medium border border-transparent rounded-bl-lg hover:text-gray-500 ml-3">
							<el-icon class="mr-2">
							<Edit/>
							</el-icon>
							编辑
						</span>
						</div>
						<el-dropdown @command="handleOperator" class="cursor-pointer flex-1">
							<div class="w-0 flex-1 flex cursor-pointer">
								<span class="hover:text-blue-500 relative -mr-px w-0 flex-1 inline-flex items-center justify-center py-2 text-sm text-gray-700 font-medium border border-transparent rounded-bl-lg hover:text-gray-500 ml-3">
									<span class="text-sm font-medium">
									<el-icon class="mr-2">
										<MoreFilled/>
									</el-icon>
									<span>更多</span>
									</span>
								</span>
							</div>

							<template #dropdown>
								<el-dropdown-menu>
									<el-dropdown-item :icon="Edit" :command="{operator: 'edit', user}">编辑</el-dropdown-item>
									<el-dropdown-item v-if="!user.enabled" :icon="VideoPlay" :command="{operator: 'enable', user}">启用</el-dropdown-item>
									<el-dropdown-item v-else :icon="VideoPause" :command="{operator: 'enable', user}">停用</el-dropdown-item>
									<div class="divider"></div>
									<el-dropdown-item :icon="Key" :command="{operator: 'pwdManager', user}">重置密码</el-dropdown-item>
									<div class="divider"></div>
									<el-dropdown-item :icon="Delete" :command="{operator: 'delete', user}">删除</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown>
					</div>
				</li>
				<li @click="addUser" class="add-storage-btn cursor-pointer col-span-1 flex flex-col text-center bg-white rounded-lg border hover:shadow">
					<div class="flex-1 flex flex-col p-8 mx-auto justify-center">
						<Plus class="h-20 text-gray-300" />
					</div>
				</li>
			</ul>
		</el-card>
	</div>
</template>

<script setup>
import {Search, Delete, Edit, Key, Lock, VideoPause, VideoPlay, View, Document, Plus, MoreFilled, CopyDocument} from '@element-plus/icons-vue'
let router = useRouter();

import useUserList from "~/composables/admin/user/user";
const {
	init, 
	loading,
	searchKey, 
	userListSearchResult,
	handleOperator, 
	addUser,
	editUser,
	deleteUser, 
	handleClose,
	userSwitchEnableStatus
} = useUserList(router);

onMounted(() => {
	init();
})

</script>

<style scoped>
.el-row {
	padding: 20px;
}

.el-form-item {
	margin-right: 50px;
}

.card-title {
	color: rgba(0, 0, 0, .45);
	font-size: 14px;
}

.card-content {
	color: rgba(0, 0, 0, .85);
	font-size: 25px;
	line-height: 30px;
}

.card-title-button {
	float: right;
	padding: 3px 0;
}

.table-search-input {
	width: 300px;
	float: right;
}

#filterForm .el-row {
	padding: 0;
}

#cacheDialog >>> .el-dialog__body {
	padding: 20px;
}

.table-edit-icon {
	margin-left: 5px;
	color: #409EFF;
	cursor: pointer;
}

.current-layout {
	color: #409EFF;
}

.user-container li {
	@apply h-[13rem]
}
</style>

<route lang="yaml">
meta:
  layout: admin
  name: 用户设置
</route>
