import {loadStorageAuthReq, saveStorageAuthReq} from "~/api/admin-storage";
import util from "~/tool/common";

let authList = ref([]);
let loading = ref(false);
import Sortable from "sortablejs";

export default function useStorageAuth(router, route) {
    let currentStorageId = route.params.storageId;

    const loadAuthData = () => {
        loadStorageAuthReq(currentStorageId).then((response) => {
            authList.value = response.data;
            if (authList.value.length === 0) {
                addAuthItem();
            }
            setSort();
        });
    }

    const setSort = () => {
        const el = document.querySelector('.z-form-body')
        Sortable.create(el, {
            draggable: '.expression-item',
            onEnd: e => {
                if (e.oldIndex === e.newIndex) {
                    return;
                }

                const currRow = authList.value.splice(e.oldIndex - 1, 1)[0];
                authList.value.splice(e.newIndex - 1, 0, currRow)
            }
        })
    }

    const saveAuthData = () => {
        let notFill = authList.value.find((value) => {
            if (util.isEmpty(value.userId) || util.isEmpty(value.permission)) {
                ElMessage.warning('请检查数据填写是否完整');
                return true;
            }
        });
        // 对用户计数，同一用户不能出现多次
        for (let i = 0; i < authList.value.length; i++) {
            let num = 0;
            let toCountId = authList.value[i].userId; 
            for (let k = 0; k < authList.value.length; k++) {
                let valueElement = authList.value[k];
                if(valueElement.userId == toCountId) {
                    num++;
                }
            }
            if(num > 1){
                ElMessage.warning('同一用户不能出现多次');
                return true;
            }
        }

        if (!notFill) {
            loading.value = true;
            saveStorageAuthReq(currentStorageId, authList.value).then(() => {
                ElMessageBox.confirm('保存成功, 是否返回存储源列表？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'success',
                    callback: action => {
                        if (action === 'confirm') {
                            router.push('/admin/storage-list');
                        }else{
                            loadAuthData();
                        }
                    }
                });
            }).finally(() => {
                loading.value = false;
            });
        }
    }

    const addAuthItem = () => {
        authList.value.push({
            userId: '',
            permission: '',
            storageId: currentStorageId
        });
    }

    const deleteAuthItem = (index) => {
        authList.value.splice(index, 1);
    }

    return {
        loading, loadAuthData, authList,
        addAuthItem, deleteAuthItem,
        saveAuthData
    }
}