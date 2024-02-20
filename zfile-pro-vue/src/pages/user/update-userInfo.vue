<template>
	<z-form :model="userItem"
	        class="zfile-storage-edit"
	        :rules="rules" ref="userEditForm">

		<template #form-title>
			<div class="flex justify-items-center">
				<span>用户信息</span>
			</div>
		</template>

		<z-form-item label="用户名" prop="username">
			<el-input disabled v-model="userItem.username"/>
		</z-form-item>

		<z-form-item label="用户中文名" prop="usernameZh">
			<el-input v-model="userItem.usernameZh"/>
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
	loadUserItemReq, userSaveReq
} from "~/api/user";
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
				ElMessage({
					message: '保存成功',
					type: 'success'
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

	// 加载指定存储源的数据
	const loadUserItem = (userId) => {
		loadUserItemReq(userId).then((res) => {
			userItem.value = res.data;
		})
	}
	// loading
	let loading = ref(false);
	const userstr = window.localStorage.getItem('zfile-user');
	const user = JSON.parse(userstr);
	let userId = user.id;
	if (userId) {
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
				callback();
			},
		}],
		usernameZh: [{required: true, message: '中文名不能为空'}],
	});

	return { userItem, loading, rules }
}
let { userItem, loading, rules } = useInitData();


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
  layout: file
  name: 修改用户信息
</route>
