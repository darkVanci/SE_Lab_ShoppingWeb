<template>
    <div class="back">
        <el-button type="primary" plain round @click="goBack">
            <el-icon style="vertical-align: middle">
                <Back />
            </el-icon>
            <span style="vertical-align: middle"> 返回 </span>
        </el-button>
    </div>
</template>

<script>
export default {
    methods: {
        goBack() {
            this.$router.push({name: 'home'})
        },
    }
}
</script>

<style scoped>
.back {
    margin:20px;
    position:absolute;
}
</style>