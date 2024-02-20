import {
    loadUserListReq,
    loadUserItemReq,
    deleteUserReq,
    resetPasswordReq,
    userSwitchEnableReq,
    userSaveReq
} from "~/api/admin-user";
import Sortable from "sortablejs";

import useCommon from "~/composables/useCommon";
const { isMobile } = useCommon();

let cacheManageVisible = ref(false);
let currentCacheManageId = ref();

let loading = ref(false);
let userList = ref([]);
let searchKey = ref('');

export default function useUserList(router) {

    // 添加用户
    const addUser = () => {
        router.push('/admin/user-edit');
    }

    // 编辑用户
    const editUser = (row) => {
        router.push('/admin/user-edit/' + row.id);
    }

    // 删除用户
    const deleteUser = (row) => {
        ElMessageBox.confirm('是否确认删除？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            callback: action => {
                if (action === 'confirm') {
                    deleteUserReq(row.id).then((response) => {
                        ElMessage.success('删除成功');
                        init();
                    });
                }
            }
        });
    }

    // 响应关闭操作提示.
    const handleClose = (done) => {
        ElMessageBox.confirm('是否确认关闭？关闭后填写的数据不会保留。', {
            type: 'warning',
        }).then(_ => {
            done();
        }).catch(_ => {
        });
    }


    // 切换存储源启用/关闭状态
    const userSwitchEnableStatus = (row) => {
        ElMessageBox.confirm(`是否确认${row.enabled ? '禁用' : '启用'}用户。`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            callback: action => {
                if (action === 'confirm') {
                    let enableStatus = row.enabled ? 'disable' : 'enable';
                    userSwitchEnableReq(row.id, enableStatus).then(() => {
                        ElMessage.success((row.enabled ? '禁用' : '启用')  + '成功');
                        row.enabled = !row.enabled;
                    })
                }
            }
        });
    }
    // 密码重置
    const showPasswordResetPage = (row) => {
        ElMessageBox.confirm('是否确认重置'+row.username+'的密码？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            callback: action => {
                if (action === 'confirm') {
                    resetPasswordReq(row.id).then((response) => {
                        ElMessage.success('重置成功');
                        init();
                    });
                }
            }
        });
    }

    // 响应操作指令
    const handleOperator = (command) => {
        switch (command.operator) {
            case "edit": editUser(command.user); break;
            case "enable": userSwitchEnableStatus(command.user); break;
            case "pwdManager":  showPasswordResetPage(command.user); break;
            case "delete": deleteUser(command.user); break;
        }
    }

    // 初始化
    const init = () => {
        loading.value = true;
        loadUserListReq().then((response) => {
            userList.value = response.data;
            loading.value = false;
        });
    }

    // 存储源列表计算属性, 用于显示右上角搜索框对应的搜索结果.
    const userListSearchResult = computed(() => {
        if (searchKey.value === '') {
            return userList.value;
        }

        // 如果名字或备注或存储源类型包含输入关键字，则作为检索结果.
        return userList.value.filter((item) => {
            if (item.username && item.username.toLowerCase().indexOf(searchKey.value.toLowerCase()) !== -1) {
                return true;
            }
            if (item.remark && item.remark.toLowerCase().indexOf(searchKey.value.toLowerCase()) !== -1) {
                return true;
            }
            if (item.usernameZh && item.usernameZh.toLowerCase().indexOf(searchKey.value.toLowerCase()) !== -1) {
                return true;
            }
            return false;
        });
    })


    return {
        init, 
        loading,
        searchKey, 
        userListSearchResult,
        handleOperator,
        addUser, editUser, deleteUser, handleClose,
        userSwitchEnableStatus,
    }
}
