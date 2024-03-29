<template>
	<div class="zfile-header">
    <div class="zfile-header-breadcrumb box animate__animated animate__fadeIn flex flex-1">
      <header-logo></header-logo>
    </div>

		<div class="zfile-header-right box animate__animated animate__fadeIn" v-if="isNotMobile">
      <!-- 功能区 -->
			<div class="zfile-header-btn">

        <!-- debug 模式 -->
        <template v-if="storageConfigStore.globalConfig.debugMode">
          <el-tooltip placement="bottom">
            <template #content>
              此功能为 DEBUG 模式下重置管理员密码功能, 使用完后请关闭 DEBUG 模式并重启服务.
            </template>
            <el-button size="default" @click="resetAdminPwd" type="danger" >
              重置管理员密码
            </el-button>
          </el-tooltip>
        </template>

        <!-- 设置按钮 -->
        <el-tooltip placement="bottom">
					<template #content>
						视图设置
					</template>
					<div @click="openSettingVisible">
            <View style="width: 20px; height: 20px;" />
					</div>
				</el-tooltip>

        <!-- 后台登录 -->
				<el-tooltip placement="bottom" v-if="storageConfigStore.globalConfig.showLogin && role=='系统管理员'">
					<template #content>
						后台管理
					</template>
					<div @click="toLoginView">
            <i-custom-tool-setting></i-custom-tool-setting>
						<!-- <svg-icon class="text-2xl text-gray-500 hover:text-blue-500" name="login"></svg-icon> -->
					</div>
				</el-tooltip>
			</div>

      <!-- 用户信息 -->
      <div class="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
        <el-dropdown trigger="click" popper-class="zfile-header-dropdown">
					<div>
					  <img class="h-8 w-8 rounded-full" src="../../assets/image/avator.png" alt=""/>
					</div>
					<template #dropdown>
						<el-dropdown-menu class="font-medium">
              <el-dropdown-item>
								欢迎您,{{ user.username+'('+user.usernameZh+')' }}
							</el-dropdown-item>
							<el-dropdown-item @click="common.openPage('https://docs.zfile.vip')">
								ZFile Docs
							</el-dropdown-item>
              <el-dropdown-item @click="common.openPage('https://github.com/zhaojun1998/zfile')">
                ZFile Github
              </el-dropdown-item>
              <el-dropdown-item @click="router.push('/user/update-password')">
                  修改密码
              </el-dropdown-item>
              <el-dropdown-item @click="router.push('/user/update-userInfo')">
                  修改用户信息
              </el-dropdown-item>
              <el-dropdown-item @click="logout">
                <a href="#" >注销</a>
              </el-dropdown-item>
						</el-dropdown-menu>
					</template>
				</el-dropdown>
      </div>
		</div>

    <div v-if="isMobile" v-show="route.params.storageKey">
      <el-dropdown trigger="click" class="top-3">
        <Bars3Icon class="block h-6 w-6" aria-hidden="true"/>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>
              欢迎您,{{ user.username+'('+user.usernameZh+')' }}
            </el-dropdown-item>
            <el-dropdown-item v-if="storageConfigStore.globalConfig.showLogin && role=='系统管理员'" @click="toLoginView">
              <svg-icon class="text-base mr-2 text-gray-500" name="login"></svg-icon>
              后台管理
            </el-dropdown-item>
            <el-dropdown-item @click="router.push('/user/update-password')">
                修改密码
            </el-dropdown-item>
            <el-dropdown-item @click="router.push('/user/update-userInfo')">
                修改用户信息
            </el-dropdown-item>
            <el-dropdown-item @click="openSettingVisible">
              <svg-icon class="text-base mr-2 text-gray-500" name="tool-setting"></svg-icon>
              更多设置
            </el-dropdown-item>
            <el-dropdown-item @click="logout">
              <a href="#" >注销</a>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
	<Setting></Setting>
</template>

<script setup>
import { Bars3Icon } from '@heroicons/vue/24/outline'
import common from "~/common";

let router = useRouter();
let route = useRoute();

// debug 模式相关操作.
import useHeaderDebugMode from "~/composables/header/useHeaderDebugMode";
const { resetAdminPwd } = useHeaderDebugMode();

import useStorageConfigStore from "~/stores/storage-config";
let storageConfigStore = useStorageConfigStore();

import useFileDataStore from "~/stores/file-data";
let fileDataStore = useFileDataStore();

import {logoutReq} from '~/api/login'

// 系统管理员才显示后台管理
let role = ref(null);
let user = {
  role: '普通用户',
  username: 'anonymous',
  usernameZh: '游客'
}
const userstr = window.localStorage.getItem('zfile-user');
if(userstr != null){
  user = JSON.parse(userstr);
  role.value = user.role;
}

import useSetting from "~/composables/header/useSetting";
const { openSettingVisible } = useSetting();

import useCommon from "~/composables/useCommon";
const { isNotMobile, isMobile, encodeAllIgnoreSlashes } = useCommon();

import useFileData from "~/composables/file/useFileData";
let { initStorageConfig } = useFileData();

// 监听存储源设置 -> 默认打开图片模式, 如果为是, 则打开图片模式.
watch(() => [storageConfigStore.folderConfig.defaultSwitchToImgMode, fileDataStore.oldStorageKey], (val, oldValue) => {
  let defaultSwitchToImgMode = val[0];
  let storageKey = val[1];
  let oldStorageKey = oldValue[1];

  if (storageKey !== oldStorageKey) {
    fileDataStore.imgMode = defaultSwitchToImgMode;
  }
})

const toLoginView = () => {
	// window.location.href = '/login'
  router.push('/admin')
}

// 注销
const logout = () => {
    logoutReq().then(() => {
        router.push('/login')
    })
}

// 自定义 css js 功能.
import { useStyleTag } from '@vueuse/core'
if (storageConfigStore.globalConfig.customCss) {
  try {
	  useStyleTag(storageConfigStore.globalConfig.customCss);
  } catch (e) {
    console.error('加载自定义 css 加载失败:', storageConfigStore.globalConfig.customCss, e);
  }
}
import HeaderLogo from "~/components/file/HeaderLogo.vue";

const loadScriptDom = (scriptDom) => {
  if (scriptDom) {
    document.getElementsByTagName('head')[0].appendChild(scriptDom);
    console.log(`加载自定义 js, src:${scriptDom.src}, text:${scriptDom.text}`);
  }
}

const loadScriptText = (scriptText) => {
  if (scriptText && scriptText.trim()) {
    let scriptDom = document.createElement('script');
    scriptDom.type = 'text/javascript';
    scriptDom.text = scriptText;
    loadScriptDom(scriptDom);
  }
}


onMounted(() => {
  nextTick(()=>{
    if (storageConfigStore.globalConfig.customJs) {
      // 创建一个新的 div 元素
      const tempDivDom = document.createElement('div');
      // 将字符串作为 HTML 插入到新的 div 中
      tempDivDom.innerHTML = storageConfigStore.globalConfig.customJs;
      // 遍历新 div 中的所有 script 元素
      Array.from(tempDivDom.getElementsByTagName('script')).forEach(script => {
        // 如果该 script 元素有 src 属性，则动态引入外部 JavaScript
        if (script.src) {
          const newScript = document.createElement('script');
          for (let i = 0; i < script.attributes.length; i++) {
            const attr = script.attributes[i];
            newScript.setAttribute(attr.name, attr.value);
          }
          loadScriptDom(newScript);
        } else {
          // 否则，执行内联的 JavaScript 代码
          loadScriptText(script.innerHTML);
        }
        tempDivDom.removeChild(script);
      });
      loadScriptText(tempDivDom.innerHTML);
    }
  })
})


</script>

<style scoped lang="scss">

.zfile-header {
	display: flex;
	flex-flow: row nowrap;
	justify-content: space-between;
	height: 48px;
	line-height: 48px !important;
	padding: 0 15px;

	background-color: #ffffff;
	color: #606266;
	transition: border-color var(--el-transition-duration), background-color var(--el-transition-duration);
	border-bottom: 1px solid rgba(132, 133, 141, 0.2);

	.el-scrollbar {
    @apply w-full;
		:deep(.el-scrollbar__bar.is-vertical) {
			display: none !important;
		}
	}

	.zfile-header-breadcrumb {
		:deep(.el-breadcrumb) {
			line-height: 48px;
			font-size: 13px;
			white-space: nowrap;
			margin-left: 14px;

			.el-breadcrumb__item {
				display: inline;
				float: none;
			}
		}
	}

	.zfile-header-right {
		@apply flex space-x-10;

    :deep(.el-dropdown) {
      line-height: 48px !important;
    }

		.zfile-header-btn {
			@apply flex text-4xl space-x-10 items-center;

			div {
				@apply cursor-pointer h-5 #{!important};
			}
		}

		.zfile-header-storage-select {
			@apply mr-4 min-w-[200px];
		}
	}

}

@media only screen and (max-width: 767px) {
	.zfile-header {
		:deep(.el-breadcrumb__separator) {
			display: none !important;
		}

		:deep(.el-form-item__label) {
			display: none !important;
		}

		:deep(.el-select) {
			width: 120px;
      @apply truncate text-sm font-medium text-gray-700;
		}
	}
}

.zfile-debug-tips {
	:deep(.el-form-item__label) {
		font-weight: bold;
		color: red !important;
	}
}


.zfile-header-storage-select {
  :deep(.el-input__wrapper) {
    @apply truncate text-sm font-medium;
  }
}

</style>

<style lang="scss">
.zfile-header-dropdown {
	.el-dropdown-menu__item:hover,
	.el-dropdown-menu__item:hover svg {
		@apply text-blue-500
	}
  .el-dropdown-menu__item{
    i.dropdown-item-icon{
      font-size: 17px;
      margin-right: 12px;
    }
  }
}

</style>
