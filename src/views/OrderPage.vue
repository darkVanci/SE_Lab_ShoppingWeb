<template>
    <div class="back">
        <el-button type="primary" plain round>
            <el-icon style="vertical-align: middle">
                <Back />
            </el-icon>
            <span style="vertical-align: middle"> 返回 </span>
        </el-button>
    </div>
    <div class="checkout-page">
        <h1>订单确认</h1>
        <!-- 商品信息 -->
        <div class="products">
            <h2>商品信息</h2>
            <table>
                <thead>
                    <tr>
                        <th>商品名称</th>
                        <th>购买件数</th>
                        <th>店铺名称</th>
                        <th>单价</th>
                        <th>备注</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(product, index) in products" :key="index">
                        <td>{{ product.name }}</td>
                        <td>{{ product.quantity }}</td>
                        <td>{{ product.store }}</td>
                        <td>{{ product.price }}</td>
                        <td>{{ product.note }}</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- 收货信息 -->
        <div class="shipping">
            <h2>收货信息</h2>
            <label for="address">选择收货地址：</label>
            <select id="address" name="address" v-model="selectedAddress">
                <option v-for="(address, index) in addresses" :key="index" :value="address">{{ address }}</option>
            </select>
            <button @click="showNewAddressForm = true">新建收货地址</button>
            <div v-show="showNewAddressForm">
                <label for="new-address">新建收货地址：</label>
                <input type="text" id="new-address" v-model="newAddress">
                <button @click="addNewAddress">保存</button>
            </div>
        </div>

        <!-- 优惠券 -->
        <div class="coupons">
            <h2>优惠券</h2>
            <label for="coupon">选择优惠券：</label>
            <select id="coupon" name="coupon" v-model="selectedCoupon">
                <option v-for="(coupon, index) in coupons" :key="index" :value="coupon">{{ coupon }}</option>
            </select>
        </div>

        <!-- 结算金额及优惠明细 -->
        <div class="summary">
            <h2>结算金额及优惠明细</h2>
            <table>
                <tbody>
                    <tr>
                        <td>原商品总价：</td>
                        <td>{{ totalPrice }}</td>
                    </tr>
                    <tr v-for="(discount, index) in discounts" :key="index">
                        <td>{{ discount.name }}：</td>
                        <td>{{ discount.amount }}</td>
                    </tr>
                    <tr>
                        <td>总金额：</td>
                        <td>{{ finalPrice }}</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- 提交订单 -->
        <button @click="submitOrder">提交订单</button>
    </div>
</template>
  
<script>
export default {
    data() {
        return {
            products: [
                {
                    name: '商品1',
                    quantity: 2,
                    store: '店铺1',
                    price: 100,
                    note: '备注1'
                },
                {
                    name: '商品2',
                    quantity: 1,
                    store: '店铺2',
                    price: 200,
                    note: '备注2'
                },
                {
                    name: '商品3',
                    quantity: 3,
                    store: '店铺3',
                    price: 300,
                    note: '备注3'
                }
            ],
            addresses: ['地址1', '地址2', '地址3'],
            selectedAddress: '',
            showNewAddressForm: false,
            newAddress: '',
            coupons: ['优惠券1', '优惠券2', '优惠券3'],
            selectedCoupon: '',
            discounts: [],
        };
    },
    computed: {
        totalPrice() {
            return this.products.reduce((total, product) => total + product.quantity * product.price, 0);
        },
        finalPrice() {
            return this.totalPrice - this.discounts.reduce((total, discount) => total + discount.amount, 0);
        }
    },
    methods: {
        addNewAddress() {
            this.addresses.push(this.newAddress);
            this.selectedAddress = this.newAddress;
            this.showNewAddressForm = false;
        },
        submitOrder() {
            // TODO: 提交订单
        }
    },
    watch: {
        selectedCoupon(newCoupon) {
            // 更新优惠明细
            this.discounts = [];

            if (newCoupon === '') {
                return;
            }

            switch (newCoupon) {
                case '优惠券1':
                    this.discounts.push({
                        name: '优惠券1',
                        amount: 50
                    });
                    break;
                case '优惠券2':
                    this.discounts.push({
                        name: '优惠券2',
                        amount: 100
                    });
                    break;
                case '优惠券3':
                    this.discounts.push({
                        name: '优惠券3',
                        amount: 150
                    });
                    break;
                default:
                    break;
            }
        }
    }
}
</script>

<style>
.back {
    float: left;
}

.checkout-page {
    max-width: 800px;
    margin: 0 auto;
    font-family: Arial, sans-serif;
}

h1 {
    text-align: center;
    font-size: 36px;
    margin-bottom: 30px;
}

h2 {
    font-size: 24px;
    margin-top: 40px;
}

table {
    border-collapse: collapse;
    width: 100%;
    margin-top: 20px;
}

th,
td {
    text-align: left;
    padding: 10px;
    border: 1px solid #ddd;
}

th {
    background-color: #f2f2f2;
}

label {
    display: block;
    margin-top: 20px;
}

select {
    width: 100%;
    padding: 10px;
    margin-top: 10px;
    margin-bottom: 20px;
    border-radius: 5px;
    border: none;
    box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
}

button {
    padding: 10px 20px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-left: 20px;
}

button:hover {
    background-color: #0069d9;
}

.summary {
    margin-top: 40px;
}

.summary table {
    margin-top: 20px;
}

.summary td:nth-child(1) {
    font-weight: bold;
    padding-right: 20px;
}

.summary td:nth-child(2) {
    font-weight: bold;
}

button[type="submit"] {
    margin-top: 40px;
}

.title {
    font-weight: bold;
    font-size: 18px;
    margin-left: 20px;
}
</style>