import { resolve } from 'path'
import { env } from './shared/env'
import Vue from '@vitejs/plugin-vue'
import Pages from 'vite-plugin-pages'
import Icons from 'unplugin-icons/vite'
import Inspect from 'vite-plugin-inspect'
import Markdown from './plugins/markdown'
// import Windicss from 'vite-plugin-windicss'
import vueJsx from '@vitejs/plugin-vue-jsx'
import Removelog from 'vite-plugin-removelog'
import ViteRestart from 'vite-plugin-restart'
import I18n from '@intlify/vite-plugin-vue-i18n'
import { viteMockServe } from 'vite-plugin-mock'
import Layouts from 'vite-plugin-vue-meta-layouts'
import AutoImport from 'unplugin-auto-import/vite'
import IconsResolver from 'unplugin-icons/resolver'
import {FileSystemIconLoader} from "unplugin-icons/loaders";
import Components from 'unplugin-vue-components/vite'
import viteCompression from 'vite-plugin-compression'
// import { markdownWrapperClasses } from './plugins/markdown'
import legacy from '@vitejs/plugin-legacy'

import visualizer from "rollup-plugin-visualizer";

import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import path from 'path';
import {
	ArcoResolver,
	IduxResolver,
	VantResolver,
	DevUiResolver,
	QuasarResolver,
	ViewUiResolver,
	InklineResolver,
	TDesignResolver,
	NaiveUiResolver,
	Vuetify3Resolver,
	VarletUIResolver,
	LayuiVueResolver,
	PrimeVueResolver,
	HeadlessUiResolver,
	ElementPlusResolver,
	AntDesignVueResolver,
	VueUseComponentsResolver,
} from 'unplugin-vue-components/resolvers'
import Modules from 'vite-plugin-use-modules'
import { GenerateTitle } from './plugins/html'
import { AutoImportResolvers, normalizeResolvers } from './shared/resolvers'

export default () => {
	return [
		legacy({
			targets: ['defaults', 'not IE 11', 'chrome >= 49', 'firefox >= 1', 'edge > 1'],  //需要兼容的目标列表，可以设置多个
			additionalLegacyPolyfills: ['regenerator-runtime/runtime'],
			modernPolyfills: ['es.global-this'],
			renderLegacyChunks: true
		}),
		// 模块自动加载
		Modules({
			auto: true,
		}),
		// 生成 title
		GenerateTitle(),
		// vue 官方插件，用来解析 sfc
		Vue({
			include: [/\.vue$/, /\.md$/],
		}),
		// markdown 编译插件
		Markdown(),
		// 文件路由
		Pages({
			extensions: ['vue', 'md', 'tsx'],
			exclude: ['**/pages/file.vue']
			// extendRoute: route => {
			// 	if (route.path === '/:storageKey/:fullpath(.*)') {
			// 		route.path = '/:storageKey/:fullpath(.*)*'
			// 	}
			// }
		}),
		// 布局系统
		Layouts(),
		// 调试工具
		Inspect({
			enabled: env.VITE_APP_INSPECT,
		}),
		// windicss 插件
		// Windicss({
		// 	safelist: markdownWrapperClasses
		// }),
		// mock 服务
		viteMockServe({
			prodEnabled: env.VITE_APP_MOCK_IN_PRODUCTION,
		}),
		// https://icones.netlify.app/
		Icons({
			autoInstall: true,
			compiler: 'vue3',
			customCollections: {
				// 这里是存放svg图标的文件地址，custom是自定义图标库的名称
				custom: FileSystemIconLoader('./src/assets/icons')
			}
		}),
		// 组件自动按需引入
		Components({
			extensions: ['vue', 'md', 'tsx'],
			include: [/\.md$/, /\.vue$/, /\.tsx$/],
			dts: resolve(__dirname, './types/components.d.ts'),
			resolvers: normalizeResolvers({
				onlyExist: [
					[VantResolver(), 'vant'],
					[QuasarResolver(), 'quasar'],
					[DevUiResolver(), 'vue-devui'],
					[NaiveUiResolver(), 'naive-ui'],
					[Vuetify3Resolver(), 'vuetify'],
					[PrimeVueResolver(), 'primevue'],
					[ViewUiResolver(), 'view-design'],
					[LayuiVueResolver(), 'layui-vue'],
					[VarletUIResolver(), '@varlet/ui'],
					[IduxResolver(), '@idux/components'],
					[TDesignResolver(), 'tdesign-vue-next'],
					[InklineResolver(), '@inkline/inkline'],
					[ElementPlusResolver({
						importStyle: "sass",
					}), 'element-plus'],
					[HeadlessUiResolver(), '@headlessui/vue'],
					[ArcoResolver(), '@arco-design/web-vue'],
					[AntDesignVueResolver(), 'ant-design-vue'],
					[VueUseComponentsResolver(), '@vueuse/components'],
				],
				include: [IconsResolver({
					enabledCollections: ['ep'],
					customCollections: ['custom']
				})],
			}),
		}),
		// api 自动按需引入
		env.VITE_APP_API_AUTO_IMPORT &&
			AutoImport({
				dirs: [
					env.VITE_APP_API_AUTO_IMPORT && 'src/stores',
					env.VITE_APP_API_AUTO_IMPORT && 'src/composables',
				],
				dts: './presets/types/auto-imports.d.ts',
				imports: ['vue', 'pinia', 'vue-i18n', 'vue-router', '@vueuse/core'],
				resolvers: AutoImportResolvers,
				eslintrc: {
					enabled: true,
					globalsPropValue: true,
					filepath: 'presets/eslint/.eslintrc-auto-import.json',
				},
			}),
		visualizer({
			open: true,
			gzipSize: true,
			brotliSize: true
		}),
		// i18n 国际化支持
		I18n({
			runtimeOnly: true,
			compositionOnly: true,
			include: [resolve(__dirname, '../locales/**')],
		}),
		// 预设热重启服务
		ViteRestart({
			restart: ['.env*', 'presets/tov.[jt]s', 'presets/shared/**/*'],
		}),
		// tsx 支持
		vueJsx(),
		// 生产环境资源压缩
		viteCompression({
			disable: true,
			// @ts-ignore
			algorithm: env.VITE_APP_COMPRESSINON_ALGORITHM,
		}),
		// 生产环境下移除 console.log, console.warn, console.error
		// Removelog({
		//
		// }),
		createSvgIconsPlugin({
			// 指定需要缓存的图标文件夹
			iconDirs: [path.resolve(process.cwd(), 'src/assets/icons')],
			// 指定symbolId格式
			symbolId: 'icon-[dir]-[name]',
			// 自定义 dom id
			customDomId: '__svg__icons__dom__',
		}),
	]
}