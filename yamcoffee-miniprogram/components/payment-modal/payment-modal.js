Component({
  properties: {
    visible: {
      type: Boolean,
      value: false
    },
    orderNo: {
      type: String,
      value: ''
    },
    amount: {
      type: Number,
      value: 0
    },
    qrCodeUrl: {
      type: String,
      value: ''
    }
  },

  data: {
    payStatus: 'pending',
    networkError: false
  },

  methods: {
    onClose() {
      this.triggerEvent('close');
    },

    onConfirmPay() {
      this.setData({
        payStatus: 'paid_pending'
      });
      this.triggerEvent('confirmpay');
    },

    onRefreshStatus() {
      this.triggerEvent('refreshstatus');
    },

    setNetworkError(isError) {
      this.setData({ networkError: isError });
    },

    resetStatus() {
      this.setData({
        payStatus: 'pending',
        networkError: false
      });
    }
  }
});
