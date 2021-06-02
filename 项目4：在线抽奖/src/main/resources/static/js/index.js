window.vm = new Vue({
    el: '#app',
    data() {//绑定页面数据
        let validateUsername = (rule, value, callback) => {
            console.log("validateUsername:"+value)
            if (this.$refs['register-form']) {
                if (!value) {
                    return callback(new Error('用户名不能为空'));
                }else if(!/\w{3,20}/.test(value)){
                    return callback(new Error('用户名为3-20位的数字，字母和_'));
                }
            }
            callback();
        };
        let validatePass = (rule, value, callback) => {
            if (this.$refs['register-form']) {
                if (this.registerForm.checkPassword !== '') {
                    this.$refs['register-form'].validateField('checkPassword');
                }
            }
            callback();
        };
        let validatePass2 = (rule, value, callback) => {
            if (this.$refs['register-form']) {
                if (value === '') {
                    return callback(new Error('请再次输入密码'));
                } else if (value !== this.registerForm.password) {
                    return callback(new Error('两次输入密码不一致!'));
                }
            }
            callback();
        };
        let checkAge = (rule, value, callback) => {
            if (this.$refs['register-form']) {
                if (!value) {
                    // return callback(new Error('年龄不能为空'));
                } else if (!Number.isInteger(value)) {
                    return callback(new Error('请输入数字值'));
                } else if (value < 18) {
                    return callback(new Error('必须年满18岁'));
                }
            }
            callback();
        };
        let checkImage = (rule, file, callback) => {
            if (this.$refs['register-form']
                && this.registerForm.headFile
                && this.registerForm.headFile.name) {
                // console.log("checkImage");
                let name = this.registerForm.headFile.name;
                let idx = name.lastIndexOf(".");
                let format = idx == -1 ? '' : name.slice(idx + 1).toLowerCase();
                // console.log("name="+name+", size="+this.registerForm.headFile.size / 1024+"k, format="+format);
                const types = this.acceptImageType();

                const isIMG = types.indexOf(format) != -1;
                const isLt2M = this.registerForm.headFile.size / 1024 / 500 <= 1;

                if (!isIMG) {
                    return callback(new Error('上传头像图片只支持 ' + types.join('/') + ' 格式!'));
                }
                if (!isLt2M) {
                    return callback(new Error('上传头像图片大小不能超过500KB!'));
                }
            }
            callback();
        };
        return {
            loginForm: {
                username: '',
                password: '',
            },
            loginRules: {
                username: [
                    {required: true, message: "请输入用户名", trigger: ['blur', 'change']},
                ],
                password: [
                    {required: true, message: "请输入密码", trigger: ['blur', 'change']},
                ],
            },
            registerForm: {
                username: '',
                password: '',
                checkPassword: '',
                nickname: '',
                email: '',
                age: '',
                headFile: '',
            },
            registerRules: {
                username: [
                    {validator: validateUsername, trigger: ['blur', 'change']},
                ],
                password: [
                    {required: true, message: "请输入密码", trigger: ['blur', 'change']},
                    {min: 3, max: 20, message: "长度需要在3-20个字符", trigger: ['blur', 'change']},
                    {validator: validatePass, trigger: ['blur', 'change']},
                ],
                checkPassword: [
                    {max: 20, message: "长度在20个字符以内", trigger: ['blur', 'change']},
                    {validator: validatePass2, trigger: ['blur', 'change']},
                ],
                email: [
                    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                ],
                age: [
                    {validator: checkAge, trigger: ['blur', 'change']},
                ],
                headFile: [
                    {validator: checkImage, trigger: 'change'},
                ],
            },
            activeForm: "login-form",

            loginLoading: false,
            rememberMe: false,

            registerLoading: false,
            headBlobPath: '',
            headVisible: false,
        }
    },
    methods: {
        acceptImageType(withDot){
            const dot = withDot ? '.' : '';
            return [
                dot + 'jpg',
                dot + 'jpeg',
                dot + 'png',
                dot + 'ico',
            ];
        },
        removeHead() {
            // console.log("removeHead");
            setTimeout(() => {
                this.headBlobPath = '';
                this.registerForm.headFile = '';
            }, 50);
        },
        previewHead() {
            // console.log("previewHead");
            this.headVisible = true;
        },
        changeHead(file) {
            // console.log("changeHead");
            // console.log(file)
            this.registerForm.headFile = file;
            this.$refs['register-form'].validateField('headFile', e =>{
                if (e != "") {
                    this.registerForm.headFile = '';
                    this.headBlobPath = '';
                }else{
                    this.headBlobPath = URL.createObjectURL(file.raw);
                }
            });
        },
        toggle: function(formRef){
            this.activeForm = formRef;
            this.$nextTick(function () {
                this.$refs[formRef].clearValidate();
            });
        },
        login: function(event){
            let vm = this;
            this.$refs['login-form'].validate(valid => {
                if(!valid) return false;
                vm.loginLoading = true;
                axios.post(
                    "api/user/login",
                    vm.loginForm,
                ).then(function (json) {
                    vm.loginLoading = false;
                    showSuccess(vm, '登录成功', () => {
                        window.location.href = 'setting.html';
                    });
                }).catch(e => {
                    vm.loginLoading = false;
                    ajaxError(vm, e);
                    // console.log("==================catch==================")
                    // console.log(m);
                });
            });
        },
        register: function(event){
            let vm = this;
            this.$refs['register-form'].validate(valid => {
                if(!valid) return false;
                vm.registerLoading = true;
                let data = new FormData();
                for(let k in vm.registerForm){
                    if(!vm.registerForm[k]) continue;

                    if(k == 'headFile'){
                        data.append('headFile', vm.registerForm[k].raw);
                    }
                    else if(k != 'checkPassword'){
                        data.append(k, vm.registerForm[k]);
                    }
                }

                vm.registering = true;
                axios({
                    url: "api/user/register",
                    method: 'post',
                    // contentType: '',
                    data: data,
                }).then(function (json) {
                    vm.registerLoading = false;
                    showSuccess(vm, '用户注册成功', () => {
                        vm.toggle('login-form');
                        vm.$refs['register-form'].resetFields();
                        vm.headBlobPath = '';
                    });
                }).catch(e => {
                    vm.registerLoading = false;
                    ajaxError(vm, e);
                    // console.log("==================catch==================")
                    // console.log(m);
                });
            });
        },
    },
    created(){

    },
});