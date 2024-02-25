<template>
  <div class="zfile-index-body-wrapper" @contextmenu="showFileMenu">
    <div class="zfile-index-body"
         :class="'zfile-index-table-' + storageConfigStore.globalConfig?.layout">
      <div class="zfile-header">
        <breadcrumb class="h-12" :items="breadcrumbData" @breadcrumb-click="clickBreadcrumb">
        </breadcrumb>
        <div class="zfile-header-right box animate__animated animate__fadeIn" v-if="isNotMobile">
          <!-- 文件搜索 -->
          <div class="searchKey">
            <el-input placeholder="请输入搜索内容" style="width:15em;" v-model="fileDataStore.searchKey" clearable :suffix-icon="Search"></el-input>
          </div>

          <div class="zfile-header-btn">
            <!-- 上传 -->
            <el-dropdown v-if="storageConfigStore.permission.upload || storageConfigStore.permission.newFolder" trigger="click"
                        popper-class="zfile-header-dropdown">
              <div v-show="route.params.storageKey">
                <el-badge :value="uploadProgressInfoStatistics.totalUploadingAndWaiting"
                          :hidden="uploadProgressInfoStatistics.totalUploadingAndWaiting === 0"
                          :max="99"
                          class="!block">
                  <svg-icon class="text-2xl text-gray-500 hover:text-blue-500" name="add"></svg-icon>
                </el-badge>
              </div>
              <template #dropdown>
                <el-dropdown-menu class="font-medium">
                  <el-dropdown-item v-if="storageConfigStore.permission.newFolder" @click="newFolder">
                    <svg-icon class="text-[17px] mr-3" name="add-folder"></svg-icon>
                    新建文件夹
                  </el-dropdown-item>

                  <template v-if="storageConfigStore.permission.upload">
                    <el-dropdown-item @click="openUploadDialog" :divided="storageConfigStore.permission.upload && storageConfigStore.permission.newFolder">
                      <svg-icon class="text-[17px] mr-3" name="upload"></svg-icon>
                      上传文件
                    </el-dropdown-item>

                    <el-dropdown-item @click="openUploadFolderDialog">
                      <svg-icon class="text-[17px] mr-3" name="upload-folder"></svg-icon>
                      上传文件夹
                    </el-dropdown-item>
                  </template>

                  <el-dropdown-item @click="openMoveDialog" v-if="storageConfigStore.permission.move">
                    <el-icon class="dropdown-item-icon"><DocumentRemove /></el-icon>
                    移动文件
                  </el-dropdown-item>

                  <el-dropdown-item @click="openMoveFolderDialog" v-if="storageConfigStore.permission.moveFolder">
                    <el-icon class="dropdown-item-icon"><FolderRemove /></el-icon>
                    移动文件夹
                  </el-dropdown-item>

                  <el-dropdown-item @click="openCopyDialog" v-if="storageConfigStore.permission.copy">
                    <el-icon class="dropdown-item-icon"><DocumentCopy /></el-icon>
                    复制文件
                  </el-dropdown-item>

                  <el-dropdown-item @click="openCopyFolderDialog" v-if="storageConfigStore.permission.copyFolder">
                    <el-icon class="dropdown-item-icon"><CopyDocument /></el-icon>
                    复制文件夹
                  </el-dropdown-item>

                  <el-dropdown-item v-if="storageConfigStore.permission.arrangeFolder" :divided="storageConfigStore.permission.arrangeFolder">
                    <el-dropdown type=""  placement="left-start" trigger="hover" popper-class="zfile-header-dropdown">
                      <div style="display:flex;">
                          <el-icon class="dropdown-item-icon" ><ArrowLeft /></el-icon>
                          文件夹整理
                      </div>
                      <template  #dropdown>
                        <el-dropdown-menu>
                          <el-dropdown-item @click="unFolder" divided>
                            <el-icon class="dropdown-item-icon"><FolderOpened /></el-icon>
                            解散文件夹
                          </el-dropdown-item>

                          <el-dropdown-item @click="openRepeatDialog" divided>
                            <el-icon class="dropdown-item-icon"><DocumentCopy /></el-icon>
                            查找重复文件
                          </el-dropdown-item>

                          <el-dropdown-item @click="arrangeFolderByTime('yyyy')" divided>
                            <el-icon class="dropdown-item-icon"><Calendar /></el-icon>
                            按照年整理文件夹
                          </el-dropdown-item>

                          <el-dropdown-item @click="arrangeFolderByTime('yyyy/MM')">
                            <el-icon class="dropdown-item-icon"><Calendar /></el-icon>
                            按照年月整理文件夹
                          </el-dropdown-item>

                          <el-dropdown-item @click="arrangeFolderByTime('yyyy/MM/dd')">
                            <el-icon class="dropdown-item-icon"><Timer /></el-icon>
                            按照年月日整理文件夹
                          </el-dropdown-item>

                          <el-dropdown-item @click="arrangeFolderByAddress('国')" divided>
                            <el-icon class="dropdown-item-icon"><Position /></el-icon>
                            按照国整理文件夹
                          </el-dropdown-item>
                          <el-dropdown-item @click="arrangeFolderByAddress('国省')">
                            <el-icon class="dropdown-item-icon"><Position /></el-icon>
                            按照国省整理文件夹
                          </el-dropdown-item>
                          <el-dropdown-item @click="arrangeFolderByAddress('省')">
                            <el-icon class="dropdown-item-icon"><Position /></el-icon>
                            按照省整理文件夹
                          </el-dropdown-item>
                          <el-dropdown-item @click="arrangeFolderByAddress('省市')">
                            <el-icon class="dropdown-item-icon"><Position /></el-icon>
                            按照省市整理文件夹
                          </el-dropdown-item>
                          <el-dropdown-item @click="arrangeFolderByAddress('省市县')">
                            <el-icon class="dropdown-item-icon"><Position /></el-icon>
                            按照省市县整理文件夹
                          </el-dropdown-item>

                          <el-dropdown-item @click="arrangeFolderByFileType" divided>
                            <el-icon class="dropdown-item-icon"><Document /></el-icon>
                            按照文件类型整理文件夹
                          </el-dropdown-item>
                        </el-dropdown-menu>
                      </template>
                    </el-dropdown>
                  </el-dropdown-item>
                  

                  <template v-if="storageConfigStore.permission.arrangeFolder">
                    
                  </template>
                </el-dropdown-menu>
                
              </template>
            </el-dropdown>

            <!-- 画廊模式切换 -->
            <div v-show="route.params.storageKey" @click="fileDataStore.imgMode = !fileDataStore.imgMode">
              <svg-icon v-if="fileDataStore.imgMode" class="text-4xl" name="img-enable"></svg-icon>
              <svg-icon v-else class="text-4xl" name="img-disable"></svg-icon>
            </div>

            <!-- webdavInfo切换 -->
            <div v-show="route.params.storageKey && storageConfigStore.folderConfig.enableWebdav" @click="webdavInfoShow = !webdavInfoShow" >
              <el-icon :size="24" style="top:-0.4em;" :color="webdavInfoIconColor"><InfoFilled /></el-icon>
            </div>

          </div><!-- 右侧按钮区 -->

          <!-- 存储源选择 -->
          <div class="zfile-header-storage-select" v-if="false">
            <el-select size="default" v-model="currentStorageKey" placeholder="请选择存储源">
              <el-option v-for="item in storageList"
                        :key="item.key"
                        :label="item.name"
                        :value="item.key">
              </el-option>
            </el-select>
          </div>
        </div><!-- 右侧区 -->

        <div v-if="isMobile" v-show="route.params.storageKey">
          <el-dropdown trigger="click" class="top-3">
            <Bars3Icon class="block h-6 w-6" aria-hidden="true"/>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-if="storageConfigStore.globalConfig.showLogin && role=='系统管理员'" @click="toLoginView">
                  <svg-icon class="text-base mr-2 text-gray-500" name="login"></svg-icon>
                  后台管理
                </el-dropdown-item>
                <el-dropdown-item v-if="storageConfigStore.permission.newFolder" @click="newFolder">
                  <svg-icon class="text-base mr-2 text-gray-500" name="add-folder"></svg-icon>
                  新建文件夹
                </el-dropdown-item>
                <el-dropdown-item v-if="storageConfigStore.permission.upload" @click="openUploadDialog">
                  <svg-icon class="text-base mr-2 text-gray-500" name="upload"></svg-icon>
                  上传文件
                </el-dropdown-item>
                <el-dropdown-item v-if="storageConfigStore.permission.upload" @click="openUploadFolderDialog">
                  <svg-icon class="text-base mr-2 text-gray-500" name="upload-folder"></svg-icon>
                  上传文件夹
                </el-dropdown-item>
                <el-dropdown-item v-if="!fileDataStore.imgMode" @click="fileDataStore.imgMode = true">
                  <svg-icon class="text-base mr-2 text-gray-500" name="image"></svg-icon>
                  打开画廊模式
                </el-dropdown-item>
                <el-dropdown-item v-else-if="fileDataStore.imgMode" @click="fileDataStore.imgMode = false">
                  <svg-icon class="text-base mr-2 text-gray-500" name="image"></svg-icon>
                  关闭画廊模式
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div><!-- head区 -->

      <!-- 公告 -->
      <el-alert v-if="storageConfigStore.globalConfig.showAnnouncement && storageConfigStore.globalConfig.announcement" class="zfile-index-announcement" type="success">
        <v-md-preview :text="storageConfigStore.globalConfig.announcement"></v-md-preview>
      </el-alert>
    
      <!-- 文档模式显示 -->
      <el-card v-if="storageConfigStore.globalConfig.showDocument
                && route.params.storageKey
                && storageConfigStore.folderConfig.readmeDisplayMode === 'top'
                && storageConfigStore.folderConfig.readmeText" class="my-3" >
        <v-md-preview :text="storageConfigStore.folderConfig.readmeText"></v-md-preview>
      </el-card>

      <!-- 公告 -->
      <el-alert v-if="storageConfigStore.folderConfig.enableWebdav && webdavInfoShow" 
              class="zfile-index-announcement" type="info" show-icon>
        <span>webdav已开启，地址为</span>
        <a :href="webdavUrl"
          style="color: green;margin-left:0.5em;">
        {{ webdavUrl }}</a>
      </el-alert>

      <!-- 文件区 -->
      <el-table
        v-if="!fileDataStore.imgMode"
        id="ListTable"
        ref="fileTableRef"
        v-loading="basicLoading"
        element-loading-text="拼命加载中"
        element-loading-background="rgba(255, 255, 255, 0.6)"
        @sort-change="sortChangeMethod"
        @row-click="tableClickRow"
        @row-dblclick="tableDbClickRow"
        @cell-mouse-enter="tableHoverRow"
        @cell-mouse-leave="tableLeaveRow"
        :size="storageConfigStore.globalConfig?.tableSize"
        empty-text=""
        @row-contextmenu="showFileMenu"
        :row-class-name="tableRowClassName"
        :class="{'zfile-table-empty': filterTableData.length === 0}"
        @selection-change="selectRowsChange"
        :data="skeletonLoading ? skeletonData : filterTableData">
        <template #empty>
          <div v-show="!basicLoading">
            <svg-icon class="empty-icon" name="empty"/>
            <div class="font-bold text-base">数据为空，请先上传或添加文件</div>
          </div>
        </template>

        <el-table-column width="30px" type="selection" :selectable="checkSelectable">
        </el-table-column>

        <el-table-column
          prop="name"
          sortable="custom"
          class-name="zfile-table-col-name"
          label-class-name="table-header-left"
          min-width="100%">
          <template #header>
            <el-icon>
              <Document/>
            </el-icon>
            <span>文件名</span>
            <!-- <el-input v-model="searchKey" size="" placeholder="请输入搜索内容" @click="searchKeyClick" :prefix-icon="Search" 
              style="width: 20em;margin-left:1em;" clearable /> -->
          </template>
          <template #default="scope">
            <div v-show="skeletonLoading">
              <el-skeleton animated>
                <template #template>
                  <el-skeleton-item variant="circle"
                                    style="vertical-align: middle;width: 18px; height: 18px; margin-right: 20px" />
                  <el-skeleton-item variant="text"
                                    style="vertical-align: middle;width: 30%;"/>
                </template>
              </el-skeleton>
            </div>
            <div v-show="!skeletonLoading">
              <svg-icon :name="'file-type-' + scope.row.icon"></svg-icon>
              {{ scope.row.name }}
            </div>
          </template>
        </el-table-column>

        <el-table-column
          prop="time"
          v-if="isNotMobile"
          sortable="custom"
          class-name="zfile-table-col-time"
          min-width="25%">
          <template #header>
            <el-icon>
              <Calendar/>
            </el-icon>
            <span>修改时间</span>
          </template>
          <template #default="scope">
            <div v-show="skeletonLoading">
              <el-skeleton animated>
                <template #template>
                  <el-skeleton-item variant="text" style="width: 60%"/>
                </template>
              </el-skeleton>
            </div>
            <div v-show="!skeletonLoading">
              {{ scope.row.time }}
            </div>
          </template>
        </el-table-column>

        <el-table-column
          prop="size"
          v-if="isNotMobile"
          class-name="zfile-table-col-size"
          sortable="custom"
          min-width="20%">
          <template #header>
            <el-icon>
              <Coin/>
            </el-icon>
            <span>大小</span>
          </template>
          <template #default="scope">
            <div v-show="skeletonLoading">
              <el-skeleton animated>
                <template #template>
                  <el-skeleton-item variant="text" style="width: 30%"/>
                </template>
              </el-skeleton>
            </div>
            <div v-show="!skeletonLoading">
              {{ common.fileSizeFilter(scope.row, null, scope.row.size) }}
            </div>
          </template>
        </el-table-column>

        <template #append>
            <load-more-file></load-more-file>
        </template>
      </el-table>

      <!-- 画廊模式 -->
      <file-gallery v-if="fileDataStore.imgMode" :key="'/'+route.params.storageKey+'/'+route.params.fullpath"></file-gallery>

      <!-- 右键菜单 -->
      <z-contextmenu></z-contextmenu>


      <!-- 视频播放器 -->
      <el-dialog draggable class="zfile-video-dialog" :destroy-on-close="true"
                 v-model="dialogVideoVisible">
        <video-player v-if="dialogVideoVisible" ref="videoPlayer"/>
      </el-dialog>

      <!-- 文本编辑器 -->
      <el-dialog draggable class="zfile-text-dialog zfile-dialog-mini-close" :destroy-on-close="true"
                 :title="fileDataStore.currentPreviewRow.name"
                 v-model="dialogTextVisible">
        <TextViewer :file-name="fileDataStore.currentPreviewRow.name"
                    :file-url="fileDataStore.currentPreviewRow.url"
                    v-if="dialogTextVisible && fileDataStore.currentPreviewRow.name.indexOf('.md') === -1"/>
        <MarkdownViewer :file-name="fileDataStore.currentPreviewRow.name"
                        :file-url="fileDataStore.currentPreviewRow.url"
                        v-if="dialogTextVisible && fileDataStore.currentPreviewRow.name.indexOf('.md') !== -1"/>
      </el-dialog>

      <!-- pdf 在线预览 -->
      <el-dialog draggable class="zfile-pdf-dialog"
                 :title="fileDataStore.currentPreviewRow.name"
                 v-model="dialogPdfVisible">
        <PdfViewer :file-name="fileDataStore.currentPreviewRow.name"
                    :file-url="fileDataStore.currentPreviewRow.url"
                    v-if="dialogPdfVisible"/>
      </el-dialog>

      <!-- office 在线预览 -->
      <el-dialog draggable class="zfile-office-dialog zfile-dialog-mini-close zfile-dialog-hidden-title"
                 :title="fileDataStore.currentPreviewRow.name"
                 v-model="dialogOfficeVisible">
        <OfficeViewer
          :file-name="fileDataStore.currentPreviewRow.name"
          :file-url="fileDataStore.currentPreviewRow.url"
          v-if="dialogOfficeVisible"/>
      </el-dialog>

      <!-- 3d 在线预览 -->
      <el-dialog draggable class="zfile-3d-dialog"
                 :title="fileDataStore.currentPreviewRow.name"
                 v-model="dialog3dVisible">
        <Three3dPreview
          :file-name="fileDataStore.currentPreviewRow.name"
          :file-url="fileDataStore.currentPreviewRow.url"
           v-if="dialog3dVisible"/>
      </el-dialog>

      <!-- 生成直链 -->
      <GenerateLink v-if="generateLinkDialogVisible"></GenerateLink>
      <GenerateLinkResult></GenerateLinkResult>

      <!-- 上传框 -->
      <ZUpload @close="loadFile"></ZUpload>

      <!-- 复制框 -->
      <ZCopy @close="loadFile"></ZCopy>

      <!-- 查找重复文件 -->
      <ZRepeat @close="loadFile"></ZRepeat>

      <!-- 音频播放器 -->
      <audio-player></audio-player>

      <!-- 回到顶部 -->
      <back-top v-show="globalConfigStore.zfileConfig.gallery.showBackTop"></back-top>

      <!-- 弹窗文档 -->
      <el-dialog draggable
                 @close="readmeDialogClose"
                 class="zfile-readme-dialog zfile-dialog-mini-close zfile-dialog-hidden-title"
                 v-if="storageConfigStore.globalConfig.showDocument
                 && storageConfigStore.folderConfig.readmeText
                 && storageConfigStore.folderConfig.readmeDisplayMode === 'dialog'
                 && showDialog(storageConfigStore.folderConfig.readmeText)" :model-value="true">
        <v-md-preview :text="storageConfigStore.folderConfig.readmeText"></v-md-preview>
      </el-dialog>

      <!-- 底部文档 -->
      <el-card class="mt-5" v-if="storageConfigStore.globalConfig.showDocument
                  && route.params.storageKey
                  && storageConfigStore.folderConfig.readmeText
                  && storageConfigStore.folderConfig.readmeDisplayMode === 'bottom'">
        <v-md-preview :text="storageConfigStore.folderConfig.readmeText"></v-md-preview>
      </el-card>

      <!-- 悬浮菜单 -->
      <hover-menu></hover-menu>
    </div>
  </div>
</template>

<script setup>
import common from "~/common";

import {
  Menu, MenuButton, MenuItem, MenuItems
} from '@headlessui/vue';
import { Search, View, Position} from '@element-plus/icons-vue'
import { Bars3Icon } from '@heroicons/vue/24/outline'

import MarkdownViewerAsyncLoading from "~/components/file/preview/MarkdownViewerAsyncLoading.vue";
import MarkdownViewerDialogAsyncLoading from "~/components/file/preview/MarkdownViewerDialogAsyncLoading.vue";
import VideoPlayerAsyncLoading from "~/components/file/preview/VideoPlayerAsyncLoading.vue";
import TextViewerAsyncLoading from "~/components/file/preview/TextViewerAsyncLoading.vue";

import useFileDataStore from "~/stores/file-data";
import useStorageConfigStore from "~/stores/storage-config";
import useGlobalConfigStore from "~/stores/global-config";
// element 弹窗消息提示相关
// element table 表格加载动画
import "~/assets/table-animation.less";
// element 图标
import { Calendar, Coin, Document } from "@element-plus/icons-vue";

import useFileContextMenu from "~/composables/file/useFileContextMenu";
const { showFileMenu } = useFileContextMenu();

// 文件类别数据相关
import useFileData from "~/composables/file/useFileData";
// 直链相关
import useFileLink from "~/composables/file/useFileLink";
// 表格相关基础操作
import useTableOperator from "~/composables/file/useTableOperator";
// 文件预览相关
import useFilePreview from "~/composables/file/useFilePreview";

import useCommon from "~/composables/useCommon";
import useFileSelect from "~/composables/file/useFileSelect";
// 文件上传相关
import useRouterData from "~/composables/useRouterData";

// 存储源列表.
import useHeaderStorageList from "~/composables/header/useHeaderStorageList";
const { loadStorageSourceList, currentStorageKey, storageList } = useHeaderStorageList();
// 面包屑数据和操作
import useBreadcrumb from "~/composables/header/useHeaderBreadcrumb";
const { buildBreadcrumbData, breadcrumbData } = useBreadcrumb();

const clickBreadcrumb = (item) => {
  if (item.href) {
    router.push(item.href);
  }
}

onMounted(() => {
	loadStorageSourceList().then(() => {
		buildBreadcrumbData();
	});
})

import useFileOperator from '~/composables/file/useFileOperator';
const { newFolder, unFolder, arrangeFolderByTime, arrangeFolderByAddress, arrangeFolderByFileType } = useFileOperator();
import useFileUpload from "~/composables/file/useFileUpload";
const { openUploadDialog, openUploadFolderDialog, uploadProgressInfoStatistics } = useFileUpload();
import useFileCopyMove from "~/composables/file/useFileCopyMove";
const { openMoveDialog, openMoveFolderDialog,openCopyDialog, openCopyFolderDialog } = useFileCopyMove();
import useFileRepeat from "~/composables/file/useFileRepeat";
const { openRepeatDialog } = useFileRepeat();

// webdavInfo
let webdavInfoShow = ref(false)
const webdavInfoIconColor = computed(() =>
  webdavInfoShow.value ? '#79bbff' : ''
)

// markdown viewer 组件懒加载, 节约首屏打开时间
const VMdPreview = defineAsyncComponent({
  loader: () => {
    return new Promise((resolve, reject) => {
      ;(async function () {
        try {
          const res = await import('@kangc/v-md-editor/lib/preview')
          import('@kangc/v-md-editor/lib/style/preview.css');
          import('@kangc/v-md-editor/lib/theme/style/github.css');
          const hljs = await import('highlight.js');
          const githubTheme = await import('@kangc/v-md-editor/lib/theme/github.js');
          res.use(githubTheme, {
            Hljs: hljs,
          });
          resolve(res)
        } catch (error) {
          reject(error)
        }
      })()
    })
  },
  loadingComponent: MarkdownViewerAsyncLoading
})

// 文件预览相关, 视频、音频、文本、图片
const VideoPlayer = defineAsyncComponent({
  loader: () => import("~/components/file/preview/VideoPlayer.vue"),
  loadingComponent: VideoPlayerAsyncLoading
})
const TextViewer = defineAsyncComponent({
  loader: () => import("~/components/file/preview/TextViewer.vue"),
  loadingComponent: TextViewerAsyncLoading
})
const MarkdownViewer = defineAsyncComponent({
  loader: () => import("~/components/file/preview/MarkdownViewer.vue"),
  loadingComponent: MarkdownViewerDialogAsyncLoading
})
const PdfViewer = defineAsyncComponent({
  loader: () => import("~/components/file/preview/PdfViewer.vue"),
  loadingComponent: MarkdownViewerDialogAsyncLoading
})
const OfficeViewer = defineAsyncComponent({
  loader: () => import("~/components/file/preview/OfficeViewer.vue"),
  loadingComponent: MarkdownViewerDialogAsyncLoading
})
const Three3dPreview = defineAsyncComponent({
  loader: () => import("~/components/file/preview/Three3dPreview.vue"),
  loadingComponent: MarkdownViewerDialogAsyncLoading
})

const FileGallery = defineAsyncComponent(() => import("~/components/file/preview/FileGallery.vue"))

const { isNotMobile, isMobile, encodeAllIgnoreSlashes } = useCommon();

let route = useRoute();
let router = useRouter();

let fileDataStore = useFileDataStore();
let storageConfigStore = useStorageConfigStore();
let globalConfigStore = useGlobalConfigStore();

const currentInstance = getCurrentInstance();

let { checkSelectable, selectRowsChange, tableRowClassName } = useFileSelect(currentInstance);

// 文件名过滤
const filterTableData = computed(() =>
  fileDataStore.fileList.filter(
    (data) =>
      !fileDataStore.searchKey ||
      data.name.toLowerCase().includes(fileDataStore.searchKey.toLowerCase())
  )
)

// 初始化时，加载文件列表
onBeforeMount(() => {
	loadFile();
})

const webdavUrl = computed(() => {
  let domain = storageConfigStore.globalConfig.domain;
  domain = domain[domain.length-1]=='/' ? domain : domain+'/';
  let path = !!route.params.fullpath && route.params.fullpath.length>0 ? '/'+route.params.fullpath.join('/') : '';
  return domain+'webdav/'+ route.params.storageKey + path;
});

// 切换存储源或路径时，重新加载文件列表
watch(() => [route.params.storageKey, route.params.fullpath], () => {
  if (route.params.storageKey === undefined) {
		currentStorageKey.value = '';
	}
  loadFile();
})

const {
  sortChangeMethod,
	basicLoading, skeletonLoading, skeletonData, loadFile } = useFileData();


const { generateLinkDialogVisible } = useFileLink();

const { tableClickRow, tableDbClickRow, tableHoverRow, tableLeaveRow } = useTableOperator();

const { dialogVideoVisible, dialogTextVisible, dialogPdfVisible, dialogOfficeVisible, dialog3dVisible } = useFilePreview();


import md5 from "md5";
let { storageKey, currentPath } = useRouterData();

const readmeDialogCache = useStorage(`zfile-readme-dialog-cache`, {});

const readmeDialogClose = () => {
  ElMessageBox.confirm('在公告变更前是否不再显示此公告?', '提示', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    draggable: true,
    callback: action => {
      if (action === 'confirm') {
        let key = (storageKey.value + "_" + currentPath.value);
        readmeDialogCache.value[key] = md5(storageConfigStore.folderConfig.readmeText);
      }
    }
  });
}

const showDialog = (readmeText) => {
  for (let key of Object.keys(readmeDialogCache.value)) {
    if (key === (storageKey.value + "_" + currentPath.value)
        && readmeDialogCache.value[key] === md5(readmeText)) {
      return false;
    }
  }

  return true;
}

const searchKeyClick = (event) => {
  event.stopPropagation();
}
</script>

<style lang="scss" scoped>

// 最外层边框
.zfile-index-body-wrapper {
  @apply h-full;
}

.zfile-index-body {
  @apply h-full md:px-4;

  // 文件行选中效果
  :deep(.select-row) {
    background-color: var(--el-table-row-hover-bg-color);
  }

  // 空白页样式
  :deep(.el-table__empty-block) {
    @apply -mt-10 mb-10;
  }
  :deep(.el-table__empty-text) {
    @apply w-full;
  }
  :deep(.empty-icon) {
    display: initial;
    @apply h-80 w-80;
  }


  // 公告显示样式
  .zfile-index-announcement {
    margin: 10px 0;

    :deep(.github-markdown-body) {
      padding: unset;
    }
  }
}

// 居中模式
.zfile-index-table-center {
  @apply w-[80%] ml-[10%];
}

// 卡片模式
.zfile-index-table-card {
  @apply w-11/12 max-w-7xl mx-auto;

  &::after {
    content: "";
    @apply top-2 h-2.5 block relative;
  }

  #ListTable {
    @apply my-5 p-5 rounded-lg shadow-lg;
  }
  :deep(.el-scrollbar__bar.is-horizontal) {
    @apply hidden;
  }
}

// 文件列表主体
.el-table {
  // 隐藏横向滚动条
  @apply overflow-y-hidden;

  &.zfile-table-empty {
    @apply h-full;
  }

  :deep(.el-checkbox) {
    margin-right: 30px;
  }

  :deep(.el-table__inner-wrapper) {
    height: 100%;
  }
  :deep(.el-table__body-wrapper) {
    height: 100%;
    font-weight: 450;
  }

  :deep(.el-table__body-wrapper .el-scrollbar__view) {
    height: 100%;
  }

	/* 表头 -- icon 位置和大小 */
	.el-table__header-wrapper .el-icon {
    @apply mr-4 top-0.5 text-sm;
	}

	/* 表身 -- 文件名列 icon 位置 */
	.el-table__body-wrapper .zfile-table-col-name svg {
		@apply relative align-middle text-xl mr-1.5 inline;
	}

	/* 表身 -- 不支持文字选中 */
	:deep(tr) {
    @apply select-none;
	}
}

// table default 模式样式
.el-table.el-table--default {

  /* 表头 -- icon 位置和大小 */
  .el-table__header-wrapper .el-icon {
    @apply text-base;
  }

	/* 表身 -- 文件名列 icon 位置 */
	.el-table__body-wrapper .zfile-table-col-name svg {
		@apply text-2xl;
	}
}

// table large 模式样式
.el-table.el-table--large {

  /* 表头 -- icon 位置和大小 */
  .el-table__header-wrapper .el-icon {
    @apply text-xl;
  }

	/* 表身 -- 文件名列 icon 位置 */
	.el-table__body-wrapper .zfile-table-col-name svg {
		@apply text-3xl;
	}
}

// dialog 相关
.zfile-index-body {

  // zfile dialog body 高度
  .zfile-dialog-body_height {
    @apply h-[80vh] sm:h-[85vh] overflow-auto;
  }

  // zfile dialog 宽度大屏
  .zfile-dialog-wide-screen {
    @apply w-11/12;
  }

  // 迷你关闭模式
  :deep(.zfile-dialog-mini-close) {
    .el-dialog__header .el-icon {
      @apply text-white;
    }
    .el-dialog__headerbtn {
      @apply mt-0 -right-3 -top-0.5 h-5 w-5
      bg-gray-600 hover:bg-blue-500
      rounded-full box-content border-2 border-solid border-white;

      :deep(svg) {
        @apply text-white font-bold;
      }
    }
  }

  // 隐藏标题模式
  :deep(.zfile-dialog-hidden-title) {
    .el-dialog__header {
      @apply p-0;
    }
    .el-dialog__title {
      @apply hidden #{'!important'};
    }
  }

  /* dialog 高度减少，标题居中 */
	:deep(.el-dialog__header) {
		@apply -mt-3 py-1 text-center ml-2;

    /* 修正去除边框后关闭按钮错位的问题 */
    .el-dialog__headerbtn {
      @apply -mt-3.5;
    }

    // 弹窗标题最多一行
    .el-dialog__title {
      @apply line-clamp-1;
    }
  }

	/* 去除 dialog 打开后默认滚动条 */
	:deep(.el-overlay-dialog) {
    @apply overflow-hidden;
	}

  // dialog 距离顶部的高度
  :deep(.el-dialog) {
    @apply mt-8 sm:mt-10 #{!important};
  }

  // 视频弹窗样式
  :deep(.zfile-video-dialog) {
    @extend .zfile-dialog-wide-screen;
    .el-dialog__body {
      @apply p-0; // 去除所有间距
    }
  }

  // 文本弹窗
  :deep(.zfile-text-dialog) {
    @extend .zfile-dialog-wide-screen;
    .el-dialog__body {
      @apply py-4 px-1; // 左右间距和上下间距
    }
  }

  // pdf 弹窗
  :deep(.zfile-pdf-dialog) {
    .el-dialog__body {
      @extend .zfile-dialog-body_height;
    }
  }

  // readme 弹窗
  :deep(.zfile-readme-dialog) {
    .el-dialog__body {
      @extend .zfile-dialog-body_height;
    }
  }

  // office 弹窗
  :deep(.zfile-office-dialog) {
    @extend .zfile-dialog-wide-screen;
    .el-dialog__body {
      @extend .zfile-dialog-body_height;
      @apply p-0;
    }
  }
}

</style>


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
			@apply flex text-4xl space-x-5 items-center;

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


<route lang="yaml">
meta:
  layout: file
</route>
