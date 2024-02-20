<template>
  <Contextmenu auto-ajust-placement ref="contextmenu">
    <template v-if="contextMenuTargetFile">
      <ContextmenuItem v-show="storageConfigStore.permission.open"
                       @click="openRow(selectRow)">
        <el-icon class="contextmenu-icon">
          <FolderOpened/>
        </el-icon>
        <label>打开</label>
      </ContextmenuItem>
      <ContextmenuItem v-show="storageConfigStore.permission.preview"
                       @click="openRow(selectRow)">
        <el-icon class="contextmenu-icon">
          <i-custom-preview/>
        </el-icon>
        <label>预览</label>
      </ContextmenuItem>
      <ContextmenuItem v-show="storageConfigStore.permission.download && selectStatistics.isSingleSelect"
                       @click="batchDownloadFile">
        <el-icon class="contextmenu-icon">
          <i-custom-download class="font-bold"/>
        </el-icon>
        <label>下载</label>
      </ContextmenuItem>
      <ContextmenuItem v-show="storageConfigStore.permission.download && selectStatistics.isMultiSelect && storageConfigStore.permission.batchDownload"
                       @click="batchDownloadFile">
        <el-icon class="contextmenu-icon">
          <i-custom-download-mult />
        </el-icon>
        <label>批量下载</label>
      </ContextmenuItem>
      <!-- <ContextmenuItem v-show="storageConfigStore.permission.download && selectStatistics.isMultiSelect && storageConfigStore.permission.packageDownload"
                       @click="batchDownloadFile(null,'package')">
        <el-icon class="contextmenu-icon">
          <i-custom-download-package/>
        </el-icon>
        <label>打包下载</label>
      </ContextmenuItem> -->
      <ContextmenuItem v-show="storageConfigStore.permission.link"
                       @click="openGenerateLinkDialog">
        <el-icon class="contextmenu-icon">
          <svg-icon class="inline" name="link"></svg-icon>
        </el-icon>
        <label>生成直链</label>
      </ContextmenuItem>
      <ContextmenuDivider v-show="storageConfigStore.permission.rename || storageConfigStore.permission.delete"></ContextmenuDivider>
      <ContextmenuItem v-show="storageConfigStore.permission.rename"
                       @click="rename">
        <el-icon class="contextmenu-icon">
          <svg-icon name="edit"/>
        </el-icon>
        <label>重命名</label>
      </ContextmenuItem>
      <!--<ContextmenuItem @click="moveTo">-->
      <!--	<el-icon class="contextmenu-icon">-->
      <!--		<svg-icon class="inline" name="move"></svg-icon>-->
      <!--	</el-icon>-->
      <!--	<label>移动</label>-->
      <!--</ContextmenuItem>-->
      <!--<ContextmenuItem-->
      <!--                 @click="copyTo">-->
      <!--	<el-icon class="contextmenu-icon">-->
      <!--		<CopyDocument/>-->
      <!--	</el-icon>-->
      <!--	<label>复制</label>-->
      <!--</ContextmenuItem>-->
      <ContextmenuItem v-if="storageConfigStore.permission.delete" @click="batchDelete">
        <el-icon class="contextmenu-icon">
          <svg-icon class="inline" name="delete"></svg-icon>
        </el-icon>
        <label>删除 {{selectRows.length > 0 ? ('(' + selectRows.length + ')') : ''}}</label>
      </ContextmenuItem>
      <ContextmenuDivider v-show="storageConfigStore.permission.newFolder || storageConfigStore.permission.upload"></ContextmenuDivider>
    </template>
    <ContextmenuItem v-show="storageConfigStore.permission.newFolder" @click="newFolder">
      <el-icon class="contextmenu-icon">
        <FolderOpened/>
      </el-icon>
      <label>新建文件夹</label>
    </ContextmenuItem>
    <ContextmenuItem v-show="storageConfigStore.permission.upload" @click="openUploadDialog">
      <el-icon class="contextmenu-icon">
        <i-custom-upload/>
      </el-icon>
      <label>上传文件</label>
    </ContextmenuItem>
    <ContextmenuItem v-show="storageConfigStore.permission.upload" @click="openUploadFolderDialog">
      <el-icon class="contextmenu-icon">
        <i-custom-upload-folder/>
      </el-icon>
      <label>上传文件夹</label>
    </ContextmenuItem>
    <ContextmenuItem v-show="storageConfigStore.permission.move" @click="openMoveDialog">
      <el-icon class="contextmenu-icon">
        <DocumentRemove />
      </el-icon>
      <label>移动文件</label>
    </ContextmenuItem>
    <ContextmenuItem v-show="storageConfigStore.permission.moveFolder" @click="openMoveFolderDialog">
      <el-icon class="contextmenu-icon">
        <FolderRemove />
      </el-icon>
      <label>移动文件夹</label>
    </ContextmenuItem>
    <ContextmenuItem v-show="storageConfigStore.permission.copy" @click="openCopyDialog">
      <el-icon class="contextmenu-icon">
        <DocumentCopy />
      </el-icon>
      <label>复制文件</label>
    </ContextmenuItem>
    <ContextmenuItem v-show="storageConfigStore.permission.copyFolder" @click="openCopyFolderDialog">
      <el-icon class="contextmenu-icon">
        <CopyDocument />
      </el-icon>
      <label>复制文件夹</label>
    </ContextmenuItem>

    <ContextmenuDivider v-if="storageConfigStore.permission.arrangeFolder"></ContextmenuDivider>
    <ContextmenuSubmenu v-if="storageConfigStore.permission.arrangeFolder" title="">    
      <template #title>
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <Management />
        </el-icon>
        <label class="ml-2.5">文件夹整理</label>
      </template>
      <ContextmenuItem @click="unFolder">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <FolderOpened/>
        </el-icon>
        <label>解散文件夹</label>
      </ContextmenuItem>
      <ContextmenuDivider></ContextmenuDivider>
      <ContextmenuItem @click="openRepeatDialog">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <DocumentCopy />
        </el-icon>
        <label>查找重复文件</label>
      </ContextmenuItem>
      <ContextmenuDivider></ContextmenuDivider>
      <ContextmenuItem @click="arrangeFolderByTime('yyyy')">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <Calendar/>
        </el-icon>
        <label>按照年整理文件夹</label>
      </ContextmenuItem>
      <ContextmenuItem @click="arrangeFolderByTime('yyyy/MM')">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <Calendar/>
        </el-icon>
        <label>按照年月整理文件夹</label>
      </ContextmenuItem>
      <ContextmenuItem @click="arrangeFolderByTime('yyyy/MM/dd')">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <Timer/>
        </el-icon>
        <label>按照年月日整理文件夹</label>
      </ContextmenuItem>
      <ContextmenuDivider></ContextmenuDivider>
      <ContextmenuItem @click="arrangeFolderByAddress('国')">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <Position/>
        </el-icon>
        <label>按照国整理文件夹</label>
      </ContextmenuItem>
      <ContextmenuItem @click="arrangeFolderByAddress('国省')">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <Position/>
        </el-icon>
        <label>按照国省整理文件夹</label>
      </ContextmenuItem>
      <ContextmenuItem @click="arrangeFolderByAddress('省')">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <Position/>
        </el-icon>
        <label>按照省整理文件夹</label>
      </ContextmenuItem>
      <ContextmenuItem @click="arrangeFolderByAddress('省市')">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <Position/>
        </el-icon>
        <label>按照省市整理文件夹</label>
      </ContextmenuItem>
      <ContextmenuItem @click="arrangeFolderByAddress('省市县')">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <Position/>
        </el-icon>
        <label>按照省市县整理文件夹</label>
      </ContextmenuItem>
      <ContextmenuDivider></ContextmenuDivider>
      <ContextmenuItem @click="arrangeFolderByFileType">
        <el-icon class="contextmenu-icon top-[1px] pt-[1px]">
          <Document/>
        </el-icon>
        <label>按照文件类型整理文件夹</label>
      </ContextmenuItem>
    </ContextmenuSubmenu>

    <ContextmenuDivider v-show="storageConfigStore.permission.newFolder || storageConfigStore.permission.upload"></ContextmenuDivider>
    <ContextmenuItem @click="reload">
      <el-icon class="contextmenu-icon">
        <i-custom-refresh/>
      </el-icon>
      <label>刷新</label>
    </ContextmenuItem>

  </Contextmenu>

</template>

<script setup>
import { FolderOpened } from "@element-plus/icons-vue";

import { Contextmenu, ContextmenuDivider, ContextmenuItem, ContextmenuSubmenu} from "v-contextmenu";
import useFileContextMenu from "~/composables/file/useFileContextMenu";
import useFileDataStore from "~/stores/file-data";
import useStorageConfigStore from "~/stores/storage-config";
import useGlobalConfigStore from "~/stores/global-config";
import useFileData from "~/composables/file/useFileData";
import useFileSelect from "~/composables/file/useFileSelect";
import useFileOperator from "~/composables/file/useFileOperator";
import useFileLink from "~/composables/file/useFileLink";
import useFileUpload from "~/composables/file/useFileUpload";
import useFileCopyMove from "~/composables/file/useFileCopyMove";
import useFileRepeat from "~/composables/file/useFileRepeat";

let fileDataStore = useFileDataStore();
let storageConfigStore = useStorageConfigStore();
let globalConfigStore = useGlobalConfigStore();

const { openRow } = useFileData();
const { selectRow, selectRows, selectStatistics, clearSelection } = useFileSelect();
const { rename, batchDownloadFile, moveTo, copyTo, newFolder, batchDelete, unFolder, 
  arrangeFolderByTime, arrangeFolderByAddress, arrangeFolderByFileType,} = useFileOperator();
const { openGenerateLinkDialog } = useFileLink();
const { openUploadDialog, openUploadFolderDialog } = useFileUpload();
const { openMoveDialog, openMoveFolderDialog,openCopyDialog, openCopyFolderDialog } = useFileCopyMove();
const { openRepeatDialog } = useFileRepeat();

const contextmenu = ref();
const { initContextMenu, contextMenuTargetFile } = useFileContextMenu();

onMounted(() => {
  initContextMenu(contextmenu);
})

const reload = () => {
  window.location.reload()
}

</script>

<style scoped lang="scss">

// 右键菜单
.v-contextmenu-item {
  // 文字和图标的距离
  :deep(label) {
    @apply ml-2.5;
  }

  // 图标位置修正为居中
  :deep(.contextmenu-icon) {
    @apply top-[1px] pt-[1px];
  }
}
</style>
