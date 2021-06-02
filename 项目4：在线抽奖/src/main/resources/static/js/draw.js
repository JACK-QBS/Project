window.vm = new Vue({
    el: '#app',
    data: {
        running: false,
        awards: [],
        batchNumber: 20,
        currentAwardIndex: -1,
        result: [],//所有获奖人员名单
        players: [],//剩余未抽奖人员
        awardLuckyPlayers: [],//本次奖项所有抽中人员
        members: [],//所有抽奖人员

        currentAwardCount: 0,//当前奖项名额
        currentResultNumber: 0,//当前奖项获奖人数
        remainingNumber: 0,//剩余抽奖人数

        //监听的属性
        // flush
        showResult: false,

        // disabledToggle: false,
        // goOn: false,
        // showResultButton: false,
        // toggleButtonText: '',
    },
    watch: {
        currentAwardIndex: function () {
            // console.log("watch currentAwardIndex")
            this.remainingNumber = this.players.length;
            let currentResult = this.result[this.currentAwardIndex];
            this.currentAwardCount = this.awards[this.currentAwardIndex].count;
            this.currentResultNumber = currentResult.length;
            this.awardLuckyPlayers = currentResult.slice(Math.floor(currentResult.length/this.batchNumber)*this.batchNumber);
            this.showResult = this.currentResultNumber == this.currentAwardCount
                && this.awards[this.currentAwardIndex]
                && this.awards[this.currentAwardIndex].showResult;
        },
    },
    computed: {
        showCurrent: function(){
            return !this.running
                        && (this.goOn
                            ||
                            this.currentResultNumber == this.currentAwardCount
                            ||
                            (this.awardLuckyPlayers.length > 0
                                &&
                                (this.awardLuckyPlayers.length < this.batchNumber
                                && this.currentResultNumber == this.currentAwardCount)))
        },
        disabledToggle: function () {
            // console.log("computed disabledToggle============")
            return this.currentResultNumber >= this.currentAwardCount || this.remainingNumber === 0;
        },
        goOn: function () {
            // console.log("goOn==============")
            // console.log(this.currentResultNumber)
            // console.log(this.currentAwardCount)
            // console.log(this.remainingNumber)
            return this.currentResultNumber < this.currentAwardCount
                        && this.currentResultNumber > 0
                        && this.remainingNumber > 0
                ;
        },
        showResultButton: function(){
            return this.currentResultNumber == this.currentAwardCount
                        && this.currentResultNumber != this.awardLuckyPlayers.length;
        },
        toggleButtonText: function () {
            if (this.running) {
                return '停止';
            }
            if (this.goOn) {
                return '继续';
            }
            return '开始';
        },
    },
    methods: {
        toggle: function () {
            if (this.running) {
                this.stop();
            } else {
                this.start();
            }
        },
        toggleResult: function () {
            this.awardLuckyPlayers = [];
            // console.log("toggleResult")
            // console.log(this.currentAwardIndex)
            // console.log(JSON.stringify(this.awards))
            let show = !this.awards[this.currentAwardIndex].showResult;
            this.awards[this.currentAwardIndex].showResult = show;
            this.showResult = show;
        },
        getSpeed: function () {
            return [0.1 * Math.random() + 0.01, -(0.1 * Math.random() + 0.01)];
        },
        start: function () {
            this.running = true;
            TagCanvas.SetSpeed('myCanvas', [5, 1]);
        },
        stop: function () {
            // console.log("method stop==============")
            this.running = false;
            TagCanvas.SetSpeed('myCanvas', this.getSpeed());
            let total = this.awards[this.currentAwardIndex].count;
            // this.result[this.currentAwardIndex] =
            //     this.result[this.currentAwardIndex] || [];
            let todo = total - this.result[this.currentAwardIndex].length;
            let N = this.batchNumber || 20;
            let awardLuckyPlayers = [];
            for (let i = 0, ln = Math.min(N, todo, this.players.length); i < ln; i++) {
                let index = this.getRandomInt(0, this.players.length - 1);
                awardLuckyPlayers.push(this.players.splice(index, 1)[0]);
                this.remainingNumber--;
            }
            //
            // if (this.players.length === 0) {
            //   this.players = copy(this.members);
            //   this.$alert('所有人员都已中奖，剩余奖品将在现场所有人里抽取', '提示')
            // }
            let vm = this;
            axios.post(
                "api/record/add/"+vm.awards[vm.currentAwardIndex].id,
                awardLuckyPlayers.map(item => item.id),
            ).then(function (json) {
                vm.awardLuckyPlayers = awardLuckyPlayers;
                vm.result.splice(
                    vm.currentAwardIndex,
                    1,
                    vm.result[vm.currentAwardIndex].concat(awardLuckyPlayers)
                );

                vm.currentResultNumber = vm.result[vm.currentAwardIndex].length;
                TagCanvas.Reload('myCanvas');
                // console.log(JSON.stringify(this.awardLuckyPlayers));
                // console.log(JSON.stringify(this.result));
            }).catch(e => {
                vm.$throw(e);
            });
        },
        goNext: function () {
            this.awardLuckyPlayers = [];
            if (this.currentAwardIndex < this.awards.length - 1) {
                this.currentAwardIndex += 1;
                this.currentAwardCount = this.awards[this.currentAwardIndex].count;
            }
        },
        getRandomInt: function (min, max) {
            min = Math.ceil(min);
            max = Math.floor(max);
            return Math.floor(Math.random() * (max - min + 1)) + min;
        },
        init: function () {
            try {
                TagCanvas.Start('myCanvas', 'tags', {
                    textColour: null,
                    dragControl: 1,
                    decel: 0.95,
                    textHeight: 14,
                    minSpeed: 0.01,
                    initial: [
                        0.1 * Math.random() + 0.01,
                        -(0.1 * Math.random() + 0.01),
                    ],
                });
            } catch (e) {
                // something went wrong, hide the canvas container
                document.getElementById('myCanvasContainer').style.display =
                    'none';
            }
        },
        onReplay: function () {
            let vm = this;
            this.$confirm('重新抽奖会清空当前奖项的结果，无法撤销！', '确定要重新抽奖吗？', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(function () {
                axios.get(
                    "api/record/delete/award",
                    {
                        params: {
                            id: vm.awards[vm.currentAwardIndex].id,
                        },
                    },
                ).then(function (json) {
                    vm.awardLuckyPlayers = [];
                    var p = vm.result.splice(vm.currentAwardIndex, 1, []);
                    vm.players = vm.players.concat(p[0]);
                    vm.remainingNumber += vm.currentResultNumber;
                    vm.currentResultNumber = 0;
                }).catch(function (e) {
                    vm.$throw(e);
                });
            }).catch(() => {
                // console.log("cancel")
            });
        },
        onKeyup: function (e) {
            // console.log("onKeyup")
            // console.log(e.key);
            // 空格或回车键
            // if (e.keyCode === 13 || e.keyCode === 32) {
            if (e.key === ' ' || e.key === "Enter") {
                this.$refs.action.$el.click();
            }
        },
        onKick: function (player, index) {
            let vm = this;
            index = index || vm.result[vm.currentAwardIndex].indexOf(player);
            this.$confirm('去掉后可继续抽一名', '去掉这名中奖者吗？', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }).then(function () {
                axios.get(
                    "api/record/delete/member",
                    {
                        params: {
                            id: player.id,
                        },
                    },
                ).then(function (json) {
                    if (vm.awardLuckyPlayers.indexOf(player) > -1) {
                        vm.awardLuckyPlayers.splice(vm.awardLuckyPlayers.indexOf(player), 1);
                    }
                    vm.result[vm.currentAwardIndex].splice(index, 1);
                    vm.currentResultNumber--;
                    vm.remainingNumber++;
                }).catch(function (e) {
                    vm.$throw(e);
                });
            }).catch(() => {
                // console.log("cancel")
            });
        },
    },
    created: function () {
        // console.log("created start")
        let vm = this;
        axios.get(
            "api/setting/query",
            // "setting/query.json",
            // "setting/query2.json",
            // "setting/query-error1.json",
            // "setting/query-error2.json",
            // "http://xx/setting/query-error2.json",
        ).then(function (json) {
            vm.batchNumber = json.data.batchNumber || 0;
            vm.awards = json.data.awards || [];
            vm.members = json.data.members || [];
            vm.players = copy(vm.members) || [];
            json.data.awards.forEach((award, index, array)=>{
                let ids = award.luckyMemberIds;
                if(!ids) {
                    vm.result[index] = [];
                    return;
                }
                vm.result[index] = !ids ? [] : vm.members.filter(function (item) {
                    return ids.indexOf(item.id) != -1;
                });
            });
            vm.currentAwardIndex = 0;
            // vm.awardLuckyPlayers = vm.result[0] || [];
            // vm.currentAwardCount = json.data.awards[0] && json.data.awards[0].count || 0;
            // vm.currentResultNumber = vm.awardLuckyPlayers.length;
            // console.log("created success")
            // console.log("result: "+JSON.stringify(vm.result))
            // console.log("awardLuckyPlayers: "+JSON.stringify(vm.awardLuckyPlayers))

            // console.log("created awards: "+JSON.stringify(vm.awards))
            // console.log("created players: "+JSON.stringify(vm.players))
            // console.log("created members: "+JSON.stringify(vm.members))
            let canvas = vm.$refs.canvas;
            canvas.width = document.body.offsetWidth;
            canvas.height = document.body.offsetHeight;
            vm.$nextTick(function () {
                vm.init();
                // TagCanvas.Reload('myCanvas');
            });
            document.body.addEventListener('keyup', vm.onKeyup)
            // console.log("created end")
        }).catch(function (e) {
            vm.$throw(e);
        });
    },
});