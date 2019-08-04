//Vue对象上下文，当中的属性可以任意获取，例如 app.$data.items
var app = new Vue({
    el: '#app',
    data: {
        isPaused: false, // 判断是否暂停
        items: [],
        fields: [
            {
                key: 'id',
                label: '消息ID'
            }, {
                key: 'exchangeName',
                label: '交换器'
            }, {
                key: 'routingKey',
                label: '路由键'
            }, {
                key: 'message',
                label: '消息'
            }, {
                key: 'firstSendTime',
                label: '第一次发送时间'
            }, {
                key: 'lastSendTime',
                label: '最后一次发送时间'
            }
        ],
        showDismissibleAlert: false
    }
});

function handleError(error){
    if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        console.log(error.response.data);
        console.log(error.response.status);
        console.log(error.response.headers);
    } else if (error.request) {
        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
        console.log(error.request);
    } else {
        // Something happened in setting up the request that triggered an Error
        console.log('Error', error.message);
    }
    console.log(error.config);
}
/**
 * 加载数据
 */
function loadMsg() {
    axios.get("rabbitmq/retry-cache/")
        .then(function (response) {
            console.log(response);
            app.$data.items = response.data;
        })
        .catch(function (error) {
            handleError(error);
        });
}

/**
 * 处理模态窗口 ok 绑定事件 @ok="handleOk"
 * @param oper
 */
function handleOk(oper) {
    axios.post("rabbitmq/retry-cache/" + oper)
        .then(function (response) {
            console.log("oper response:"+response);
            if (response.data) {
                isPaused();
            } else {
                app.$data.showDismissibleAlert = true;
            }
        })
        .catch(function (error) {
            handleError(error);
        });
}

function showModal(item, event) {
    console.log(item);
    app.$root.$emit('bv::show::modal', item ? 'startConfirm' : 'stopConfirm')
}

function isPaused(){
    axios.get("rabbitmq/retry-cache/status")
        .then(function (response) {
            console.log(response);
            app.$data.isPaused = response.data;
        })
        .catch(function (error) {
            handleError(error);
        });
}

loadMsg();
isPaused();