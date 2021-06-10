window.vm = new Vue({
    el: '#app',
    data: {
        batchNumber: 0,
        user: {
            name: '',
            head: '',
        },
        awards: {
            data: [],
            rules: {
                name: [
                    {required: true, message: "请输入名称", trigger: ['blur', 'change']},
                    {max: 20, message: "长度在20个字符以内", trigger: ['blur', 'change']},
                ],
                count: [
                    {required: true, message: "请输入数量", trigger: ['blur', 'change']},
                    {type: 'number', min: 1, message: "请输入大于1的数字", trigger: ['blur', 'change']},
                ],
                award: [
                    {required: true, message: "请输入奖品", trigger: ['blur', 'change']},
                    {max: 20, message: "长度在20个字符以内", trigger: ['blur', 'change']},
                ],
            },
        },
        members: {
            data: [],
            rules: {
                name: [
                    {required: true, message: "请输入姓名", trigger: ['blur', 'change']},
                    {max: 20, message: "长度在20个字符以内", trigger: ['blur', 'change']},
                ],
                no: [
                    {required: true, message: "请输入工号", trigger: ['blur', 'change']},
                    {max: 20, message: "长度在20个字符以内", trigger: ['blur', 'change']},
                ],
            },
        },
    },
    methods: {
        handleCommand(command) {
            if(command === 'userInfo'){
                console.log("修改个人信息")
            }else if(command === 'logout'){
                this.logout();
            }
        },
        logout: function(){
            let vm = this;
            axios.get(
                "api/user/logout",
            ).then(function (json) {
                showSuccess(vm, '注销成功', () => {
                    window.location.href = 'index.html';
                });
            }).catch(e => {
                ajaxError(vm, e);
            });
        },
        onReset: function() {
            let vm = this;
            this.$confirm(
                '重置会清空所有抽奖结果，无法撤销！',
                '确定要重置吗？',
                {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                }
            ).then(() => {
                axios.get(
                    "api/record/delete/setting",
                ).then((json) => {
                    showSuccess(vm, '操作成功');
                }).catch(e => {
                    vm.$throw(e);
                });
            }).catch(() => {
                // console.log("cancel")
            });
        },
        onChange: function (value) {
            let vm = this;
            axios.get(
                "api/setting/update",
                {
                    params: {
                        batchNumber: value,
                    }
                },
            ).catch(e => {
                vm.$throw(e);
            });
            // console.log(value);
            // localStorage.setItem('batchNumber', value);
        },
        focus: function (focusId) {
            this.$nextTick(function () {
                document.getElementById(focusId).focus();
            });
        },
        checkIfSave: function (data, focusIdPrefix, focusIdSuffix) {
            for (let i = 0; i < data.length; i++) {
                let row = data[i];
                if (row.action != 'view') {
                    let id = focusIdPrefix + i + focusIdSuffix;
                    this.$alert('存在未保存的数据，请先保存', '提示', {
                        confirmButtonText: '查看',
                    });
                    this.focus(id);
                    return false;
                }
            }
            return true;
        },
        validateField(form, index) {
            let result = true;
            for (let item of this.$refs[form].fields) {
                if (item.prop.split(".")[1] == index) {
                    this.$refs[form].validateField(item.prop, error => {
                        if (error != "") {
                            result = false;
                        }
                    });
                }
                if (!result) break;
            }
            return result;
        },
        resetField(form, index) {
            this.$refs[form].fields.forEach(item => {
                if (item.prop.split(".")[1] == index) {
                    item.resetField();
                }
            })
        },
        clickAdd: function (data, focusIdPrefix, focusIdSuffix) {
            if (!this.checkIfSave(data, focusIdPrefix, focusIdSuffix)) return;
            data.push({
                action: 'add',
            });
            this.focus(focusIdPrefix + (data.length - 1) + focusIdSuffix);
        },
        clickAddAward: function () {
            this.clickAdd(this.awards.data, 'award-', '-name');
        },
        clickAddMember: function () {
            this.clickAdd(this.members.data, 'member-', '-name');
        },
        clickModify: function (formId, data, index, focusIdPrefix, focusIdSuffix) {
            if (!this.checkIfSave(data, focusIdPrefix, focusIdSuffix)) return;
            data[index].action = 'update';
            this.focus(focusIdPrefix + index + focusIdSuffix);
            if (!this.validateField(formId, index)) return;
        },
        clickModifyAward: function (index) {
            this.clickModify('award-form', this.awards.data, index, 'award-', '-name');
        },
        clickModifyMember: function (index) {
            this.clickModify('member-form', this.members.data, index, 'member-', '-name');
        },
        saveAward: function (index) {
            if (!this.validateField('award-form', index)) return;
            let vm = this;
            let award = this.awards.data[index];
            let data = {
                name: award.name,
                count: award.count,
                award: award.award,
            };


            if(award.action == 'update') data.id = award.id;
            axios.post(
                "api/award/"+award.action,
                data,
            ).then(function (json) {
                if(award.action == 'add') vm.awards.data[index].id = json.data;
                vm.awards.data[index].action = 'view';
                showSuccess(vm, '操作成功');
            }).catch(e => {
                ajaxError(vm, e);
            });
        },
        cancel: function (formId, data, index) {
            // console.log("method cancel===============")
            // console.log(JSON.stringify(index))
            // console.log(JSON.stringify(data))
            if (data[index].action == 'add')
                data.splice(index, 1);
            else {
                this.resetField(formId, index);
                data[index].action = 'view';
            }
        },
        cancelAward: function (index) {
            this.cancel('award-form', this.awards.data, index);
        },
        cancelMember: function (index) {
            this.cancel('member-form', this.members.data, index);
        },
        deleteAward: function (index) {
            let vm = this;
            axios.get(
                "api/award/delete/"+vm.awards.data[index].id,
            ).then(function (json) {
                vm.awards.data.splice(index, 1);
                showSuccess(vm, '操作成功');
            }).catch(e => {
                ajaxError(vm, e);
            });
        },
        deleteMember: function (index) {
            let vm = this;
            axios.get(
                "api/member/delete/"+vm.members.data[index].id,
            ).then(function (json) {
                vm.members.data.splice(index, 1);
                showSuccess(vm, '操作成功');
            }).catch(e => {
                ajaxError(vm, e);
            });
        },
        saveMember: function (index) {
            if (!this.validateField('member-form', index)) return;
            let vm = this;
            let member = this.members.data[index];
            let data = {
                name: member.name,
                no: member.no,
            };
            if(member.action == 'update') data.id = member.id;
            axios.post(
                "api/member/"+member.action,
                data,
            ).then(function (json) {
                if(member.action == 'add') vm.members.data[index].id = json.data;
                vm.members.data[index].action = 'view';
                showSuccess(vm, '操作成功');
            }).catch(e => {
                ajaxError(vm, e);
            });
        },
    },
    created: function () {
        let vm = this;
        axios.get(
            "api/setting/query",
            // "setting/query.json",
            // "setting/query-error1.json",
            // "setting/query-error2.json",
            // "http://xxx/setting/query-error3.json",
        ).then(function (json) {
            vm.batchNumber = json.data.batchNumber;
            vm.awards.data = json.data.awards
                .filter(function (item) {
                    return item.id;
                })
                .map(function (item) {
                    item.action = 'view';
                    return item;
                });
            vm.user = json.data.user;

            vm.members.data = json.data.members
                .filter(function (item) {
                    return item.id;
                })
                .map(function (item) {
                    item.action = 'view';
                    return item;
                });
        }).catch(e => {
            vm.$throw(e);
        });
    },
});