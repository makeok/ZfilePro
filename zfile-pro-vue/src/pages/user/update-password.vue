<template>
	<z-form :model="passwordData"
	        v-loading="updateLoading"
	        :rules="passwordDataRules" ref="updatePasswordForm">
		<template #form-title>
			密码信息
		</template>
		<template #form-sub-title>
			此处可以修改您的登录账号密码，请妥善保管
		</template>

		<z-form-item prop="username" label="管理员账号">
			<el-input id="username" disabled :prefix-icon="User" v-model.trim="passwordData.username"/>
		</z-form-item>

		<z-form-item prop="oldPassword" label="原密码">
			<el-input id="oldPassword" type="password" show-password :prefix-icon="Key" v-model.trim="passwordData.oldPassword"/>
		</z-form-item>

		<z-form-item prop="password" label="新密码">
			<el-input id="password" type="password" show-password :prefix-icon="Key" v-model.trim="passwordData.password"/>
		</z-form-item>

		<z-form-item prop="repassword" label="重复新密码">
			<el-input id="repassword" type="password" show-password :prefix-icon="Key" v-model.trim="passwordData.repassword"/>
		</z-form-item>

		<template #footer>
			<el-button type="primary" size="default" :icon="CheckBadgeIcon" @click="submitForm">保存设置</el-button>
		</template>
	</z-form>
</template>

<script setup>
import {User, Key} from "@element-plus/icons-vue";
import {CheckBadgeIcon} from '@heroicons/vue/24/solid'

import usePassword from "~/composables/header/useUserResetPassword";
const { passwordData, updateLoading, updatePassword, passwordDataRules } = usePassword();

let updatePasswordForm = ref();

const submitForm = () => {
	updatePassword(updatePasswordForm);
}
</script>

<style scoped>
</style>

<route lang="yaml">
meta:
  layout: file
  name: 修改密码
</route>