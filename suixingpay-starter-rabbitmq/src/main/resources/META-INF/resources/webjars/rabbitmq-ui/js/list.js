//Vue对象上下文，当中的属性可以任意获取，例如 app.$data.items
var app = new Vue({
    el: '#app',
    data: {
        items: [],
        fields: [
            {
                key: 'name',
                label: '消费者名称'
            },
            {
                key: 'queueNames',
                label: '队列名称'
            },
            {
                key: 'running',
                label: 'Running'
            }, {
                key: 'opt',
                label: '操作'
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
 * 模态窗口和按钮中间数据传递变量
 */
var currentItem;

/**
 * 加载数据
 */
function loadData() {
    axios.get("rabbitmq/consumer/")
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
    var name = currentItem.name;
    axios.post("rabbitmq/consumer/" + name + "/" + oper)
      .then(function (response) {
          console.log("oper response:"+response);
          if (response.data) {
              loadData();
          } else {
              app.$data.showDismissibleAlert = true;
          }
      })
      .catch(function (error) {
          handleError(error);
      });
}

function showModal(item, event) {
    currentItem = item;
    app.$root.$emit('bv::show::modal', item.running ? 'stopConfirm' : 'startConfirm')
}

loadData();