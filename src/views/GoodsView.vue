<template>
    <div class="container">
        <div class="back">
            <el-button type="primary" plain round @click="goBack">
                <el-icon style="vertical-align: middle;">
                    <Back />
                </el-icon>
                <span style="vertical-align: middle;"> 返回 </span>
            </el-button>
        </div>
        <div v-for="(image, index) in images" :key="index">
            <img :src="image" />
        </div>
        <!-- <el-carousel :interval="4000" arrow="always">
                <el-carousel-item v-for="(image, index) in images" :key="index">
                    <img :src="image" />
                </el-carousel-item>
            </el-carousel> -->
        <div class="goods">
            <h2>商品信息</h2>
            <table>
                <tbody>
                    <tr>
                        <td>商品名称</td>
                        <td>{{ goodsInf.commodityName }}</td>
                    </tr>
                    <tr>
                        <td>商品种类</td>
                        <td>{{ goodsInf.categoryName }}</td>
                    </tr>
                    <tr>
                        <td>商品简介</td>
                        <td>{{ goodsInf.introduction }}</td>
                    </tr>
                    <tr>
                        <td>商品单价</td>
                        <td>{{ goodsInf.price }} 元</td>
                    </tr>
                    <tr>
                        <td>购买件数</td>
                        <td><el-input-number v-model="num" @change="handleChange" :min="1" :max="10000"
                                label="描述文字"></el-input-number></td>
                    </tr>
                    <tr>
                        <td>总价</td>
                        <td>{{ num * goodsInf.price }} 元</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            goodsInf: [],
            images: [],
            num: 2,
        }
    },
    mounted() {
        const token = localStorage.getItem('token')
        //获取商品信息
        this.$axios
            .post(
                '/merchant/showCommodityDataByCommodityId',
                { id: this.$route.params.id },
                {
                    headers: {
                        token: `${token}`
                    }
                }
            )
            .then((response) => {
                console.log(response.data)
                this.goodsInf = response.data.data
                // alert(this.goodsInf.price)
                this.images = this.goodsInf.imageUrl.split(',');
            })
            .catch((error) => {
                console.log(error)
            })
    },
    methods: {
        goBack() {
            this.$router.go(-1);
        },
    }
}
</script>

<style scoped>
.container {
    max-width: 1200px;
    margin: auto;
    padding: 20px;
}

.back {
    margin-bottom: 20px;
}

.goods h2 {
    margin-top: 30px;
    margin-bottom: 20px;
}

table {
    width: 100%;
    border-collapse: collapse;
}

td {
    padding: 10px;
    vertical-align: top;
    border: 1px solid #ccc;
}

img {
    width: 100%;
    height: auto;
    margin-bottom: 20px;
}
</style>