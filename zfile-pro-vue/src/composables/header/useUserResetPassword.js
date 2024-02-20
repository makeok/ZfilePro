import {resetPasswordReq} from "~/api/user";

const userstr = window.localStorage.getItem('zfile-user');
const user = JSON.parse(userstr);
const passwordData = ref({
    username: user.username,
    oldPassword: '',
    password: '',
    repassword: ''
});

const passwordDataRules = ref({
    username: [
        {required: true, message: '请输入管理员账号'},
    ],
    oldPassword: [
        {required: true, message: '请输入原密码'},
    ],
    password: [
        {
            required: true, 
            validator: (rule, value, callback) => {
                if (value==null || value === '') {
                    callback(new Error('请输入密码'))
                } else {
                    let reg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[`~!@#$%^&*()_+<>?:"{},.\/\\;'[\]])[A-Za-z\d`~!@#$%^&*()_+<>?:"{},.\/\\;'[\]]{8,}$/ ;
					if (!reg.test(value)) {
						callback(new Error('至少8位且必有数字+特殊字符+字母'));
						return;
					}
                    callback();
                }
            }
        },
    ],
    repassword: [
        {
            required: true,
            validator: (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== passwordData.value.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    let reg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[`~!@#$%^&*()_+<>?:"{},.\/\\;'[\]])[A-Za-z\d`~!@#$%^&*()_+<>?:"{},.\/\\;'[\]]{8,}$/ ;
					if (!reg.test(value)) {
						callback(new Error('至少8位且必有数字+特殊字符+字母'));
						return;
					}
                    callback();
                }
            }
        }
    ]
});

const updateLoading = ref();

export default function usePassword() {

    const updatePassword = (updatePasswordFormRef) => {
        updatePasswordFormRef.value.validate(checked => {
            if (checked) {
                updateLoading.value = true;
                resetPasswordReq(user.id,
                    {
                        password: passwordData.value.password,
                        oldPassword: passwordData.value.oldPassword
                    }
                ).then(() => {
                    ElMessage({
                        message: '保存成功',
                        type: 'success'
                    });
                    updateLoading.value = false;
                }).catch(error => {
                    updateLoading.value = false;
                })
            }
        })
    }

    onMounted(() => {

    })

    return {
        passwordData, updateLoading, updatePassword, passwordDataRules
    }
}