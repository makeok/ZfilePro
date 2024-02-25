<template>
	<el-container v-if="loadedConfig">
		<el-header>
			<Suspense>
				<template #default>
					<Header></Header>
				</template>
			</Suspense>
		</el-header>
		<el-container class="outer-container">
			<el-aside
				class="com_hide_scrollbar"
				:width="isCollapse ? '70px' : '200px'"
				>
				<el-button style="margin-bottom: 5px;color:#000;" @click="handleCollapse"><el-icon class="el-icon--right"><Menu /></el-icon></el-button>
				<el-menu
					default-active="2"
					:default-openeds="['1','2']"
					class="el-menu-vertical-demo"
					:router="true"
					:collapse="isCollapse"
					@open="handleOpen"
					@close="handleClose"
				>
					<el-sub-menu index="1">
						<template #title>
							<el-icon><Files /></el-icon>
							<span>私有云</span>
						</template>
						<el-menu-item index="1-1" route="/"><el-icon><DocumentCopy /></el-icon>网盘</el-menu-item>
						<el-menu-item index="1-2" route="/files/vedio"><el-icon><VideoPlay /></el-icon>视频</el-menu-item>
						<el-menu-item index="1-3" route="/files/images"><el-icon><Picture /></el-icon>图片</el-menu-item>
						<el-menu-item index="1-4" route="/files/mp3"><el-icon><Microphone /></el-icon>音乐</el-menu-item>
						<el-menu-item index="1-5" route="/files/docment"><el-icon><Tickets /></el-icon>文档</el-menu-item>
					</el-sub-menu>
					<el-sub-menu index="2">
						<template #title>
							<el-icon><location /></el-icon>
							<span>相册</span>
						</template>
						<el-menu-item index="2-1" route="/photos/location"><el-icon><location /></el-icon>地区相册</el-menu-item>
						<el-menu-item index="2-2" route="/photos/person"><el-icon><User /></el-icon>人物相册</el-menu-item>
						<el-menu-item index="2-3" route="/photos/normal"><el-icon><PictureFilled /></el-icon>普通相册</el-menu-item>
					</el-sub-menu>
				</el-menu>
			</el-aside>
			<el-main :class="'el-main-' + storageConfigStore.globalConfig?.layout">
				<router-view />
			</el-main>
		</el-container>
		<el-footer>
			<Footer></Footer>
		</el-footer>
	</el-container>
</template>

<script setup>
import {Menu} from "@element-plus/icons-vue";
import Header from "~/components/layout/Header.vue";
import Footer from "~/components/layout/Footer.vue";
import { loadGlobalSiteConfigReq } from "~/api/home";

import useStorageConfigStore from "~/stores/storage-config";
import common from "~/common";
let storageConfigStore = useStorageConfigStore();

let isCollapse = ref(false)

// 初始化时，加载全局设置
onBeforeMount(() => {
	loadGlobalSiteSetting()
})

let router = useRouter();

let loadedConfig = ref(false);

const loadGlobalSiteSetting = () => {
	loadGlobalSiteConfigReq().then((res) => {
		// 如果未安装, 则跳转到安装页
		if (!res.data.installed) {
			router.push('/install');
			return;
		}
		storageConfigStore.updateGlobalConfig(res.data);

		if (res.data.customAudioSuffix) {
			common.constant.fileTypeMap.audio = res.data.customAudioSuffix.split(',');
		}
		if (res.data.customImageSuffix) {
			common.constant.fileTypeMap.image = res.data.customImageSuffix.split(',');
		}
		if (res.data.customTextSuffix) {
			common.constant.fileTypeMap.text = res.data.customTextSuffix.split(',');
		}
		if (res.data.customVideoSuffix) {
			common.constant.fileTypeMap.video = res.data.customVideoSuffix.split(',');
		}
		if (res.data.customOfficeSuffix) {
			common.constant.fileTypeMap.office = res.data.customOfficeSuffix.split(',');
		}
		loadedConfig.value = true;
	}).catch((err) => {
		if (err.message === 'Network Error') {
			ElMessage.error('加载失败，无法连接到服务端，请联系管理员.');
		}
	})
}
const handleCollapse = () => {
	isCollapse.value =  !isCollapse.value
}
const handleOpen = (key, keyPath) => {

}
const handleClose = (key, keyPath) => {

}
</script>

<style lang="scss" scoped>
.el-container {
	height: 100%;
	width: 100%;

	.el-header,
	.el-footer {
		color: var(--el-text-color-primary);
		padding: 0;
		text-align: center;
	}

	.el-header {
		--el-header-height: unset;
		height: 48px;
		line-height: 48px !important;
	}

	// 去除文件区 padding
	.el-main {
		height: 100%;
		width: 100%;
		padding: 0;
		overflow-x: hidden;
	}

  .el-main-card {
    @apply bg-gray-100;
  }

	// 定义脚部高度，边框
	.el-footer {
		border-top: var(--zfile-header-footer-border-top);
		height: 40px;
		line-height: 40px;
		font-size: 14px;
	}
}
</style>
