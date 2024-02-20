<template>
	<z-form :model="userItem"
	        class="zfile-storage-edit"
	        :rules="rules" ref="userEditForm">

		<template #form-title>
			<div class="flex justify-items-center">
				<router-link to="/admin/view-user">
					<svg-icon class="inline mr-2 cursor-pointer" name="file-type-back"></svg-icon>
				</router-link>
				<span>用户信息</span>
			</div>
		</template>


		<z-form-item label="用户名" prop="username">
			<el-input v-model="userItem.username"/>
		</z-form-item>

		<z-form-item label="用户中文名" prop="usernameZh">
			<el-input v-model="userItem.usernameZh"/>
		</z-form-item>

		<z-form-item label="密码" prop="password" v-if="!isEditMode">
			<el-input type="password" show-password v-model="userItem.password"/>
		</z-form-item>


		<z-form-item label="是否启用">
			<el-switch v-model="userItem.enabled"/>
			<template #tips>
				如不启用，则在前台不显示，且不可访问.
			</template>
		</z-form-item>

		<z-form-item label="角色" prop="role">
			<el-select :disabled="!isAdminMode" filterable v-model="userItem.role" placeholder="请选择存储策略">
				<el-option :key="item"
				           v-for="(item) in supportRoleType"
				           :label="item"
				           :value="item">
				</el-option>
			</el-select>
		</z-form-item>

		<z-form-item label="备注">
			<el-input type="textarea" :rows="3"
			          placeholder="请输入备注" v-model="userItem.remark"/>
			<template #tips>
				备注信息, 用于辅助说明
			</template>
		</z-form-item>


		<template #footer>
			<el-button :loading="loading" type="primary" size="default" :icon="CheckBadgeIcon" @click="submitForm">保存</el-button>
		</template>
	</z-form>
</template>

<script setup>
import common from "~/common";

import {Link} from '@element-plus/icons-vue'
import {CheckBadgeIcon} from '@heroicons/vue/24/solid'

import region from "~/tool/region"

import ZForm from "/src/components/form/ZForm.vue";
import ZFormItem from "/src/components/form/ZFormItem.vue";

import {
	loadUserItemReq, userSaveReq, existUsernameReq
} from "~/api/admin-user";
import { ElMessageBox } from "element-plus";
import { toClipboard } from "@soerenmartius/vue3-clipboard";
import { reactive, ref } from 'vue';


const systemConfig = ref();

let router = useRouter();
let route = useRoute();

// 当前正在编辑的存储源 key
let currentEditStorageKey = null;

let userEditForm = ref()
const submitForm = async () => {
	let ret = userEditForm.value.validate((checked, fields) => {

		if (checked) {
			loading.value = true;
			userSaveReq(userItem.value).then(() => {
				ElMessageBox.confirm('保存成功, 是否返回用户列表？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'success',
					callback: action => {
						if (action === 'confirm') {
							router.push('/admin/view-user');
						}
					}
				});
			}).finally(() => {
				loading.value = false;
			})
		} else {
			ElMessage.warning('表单验证失败, 请检查表单数据是否填写正确');
			return false;
		}
	})
}

/**
 * 初始化支持的存储源列表和存储源数据
 */
let useInitData = () => {
	// 存储源数据
	let userItem = ref({
		id: null,
		username: '',
		usernameZh: '',
		password: '',
		enabled: true,
		role: '普通用户',
		remark: '',
	});

	let oldUserItem = JSON.parse( JSON.stringify(userItem.value) )

	// 加载指定存储源的数据
	const loadUserItem = (userId) => {
		loadUserItemReq(userId).then((res) => {
			userItem.value = res.data;
			oldUserItem = JSON.parse( JSON.stringify(userItem.value) )
		})
	}

	let isAdminMode = ref(true);
	let isEditMode = ref(false);

	// 支持的所有存储策略
	let supportRoleType = ref(['普通用户', '系统管理员']);

	// loading
	let loading = ref(false);

	let userId = route.params.userId;
	if (userId) {
		isEditMode.value = true;
		loadUserItem(userId)
	}

	// 表单校验规则
	let rules = ref({
		username: [{required: true, 
			validator: (rule, value, callback) => {
				if (value === undefined || value === null || value === '') {
					callback(new Error('请输入用户名'));
					return;
				}
				let reg = /^[\w\-]+$/;
				if (!reg.test(value)) {
					callback(new Error('只允许使用字母、数字、下划线、横杠'));
					return;
				}
				if(isEditMode.value){
					if(value == oldUserItem.username){
						callback();
						return;
					}
				}
				existUsernameReq({username: value}).then((res) => {
					if (res.data) {
						callback(new Error('该用户名已存在，请修改。'));
					} else {
						callback();
					}
				})
			},
		}],
		password: [
			{
				validator: (rule, value, callback) => {
					let reg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[`~!@#$%^&*()_+<>?:"{},.\/\\;'[\]])[A-Za-z\d`~!@#$%^&*()_+<>?:"{},.\/\\;'[\]]{8,}$/ ;
					if (!reg.test(value)) {
						callback(new Error('至少8位且必有数字+特殊字符+字母'));
						return;
					}
					callback()
				},
				trigger: 'change',
			}
		],
		usernameZh: [{required: true, message: '中文名不能为空'}],
		role: [{required: true, message: '角色不能为空'}],
	});

	return { userItem, supportRoleType, loading, rules, isAdminMode, isEditMode, oldUserItem }
}
let { userItem, supportRoleType, loading, rules, isAdminMode, isEditMode, oldUserItem } = useInitData();


</script>

<style scoped>

.el-drive-form-col {
    padding-left: 0 !important;
}

.zfile-site-id-input-site-type-select {
    width: 100px;
}

.zfile-info-tooltip {
	line-height: 32px;
}

.zfile-storage-edit >>> .z-form-item-input > .el-select {
	width: 100%;
}

</style>

<route lang="yaml">
meta:
  layout: admin
  name: 新增或修改用户
</route>
