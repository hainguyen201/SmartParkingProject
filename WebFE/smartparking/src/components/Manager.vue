<template>
  <el-container style="height: 100vh">
    <el-header>
      <el-row style="height: 50px;background-color: #7d7df9; border-radius: 5px 5px 5px 0px; width: 100%;top:0;">
        <el-col :span="6"
          style="color: white; font-weight: bold; margin-left: 4px; display:flex; align-items: center; height:100%">
          <span>Smart Parking System</span></el-col>
        <el-col :span="12" style="float:right; display:flex; align-items: center; height:100%">
          <el-row style="width: 100%;" type="flex" justify="end">
            <el-col :span="4">
              <el-button type="text" style="color: white; font-weight: bold">
                <i class="el-icon-user"></i>
                <span>{{userLogin.username}}</span>
              </el-button>
            </el-col>
            <el-col :span="4">
              <el-button @click="handleLogout">Đăng xuất</el-button>
            </el-col>
          </el-row>

        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-row style="height: 100%">
        <el-col :span="4" style="height: 100%">
          <el-menu class="el-menu-vertical-demo" background-color="#545c64" text-color="#fff"
            active-text-color="#ffd04b" style="height:100%">
            <el-menu-item index="1" @click="handleEntrance">
              <span>Giám sát xe ra vào </span>
            </el-menu-item>
            <el-menu-item index="2" @click="handleParking">
              <span>Quản lý bãi đỗ</span>
            </el-menu-item>
            <el-menu-item  index="3" @click="handleVehicle">
              <span>Quản lý phương tiện</span>
            </el-menu-item>
            <el-menu-item index="4" @click="handleDevice">
              <!-- <template slot="title"> -->
              <span>Quản lý thiết bị</span>
              <!-- </template>
            <el-menu-item index="4-1">Camera</el-menu-item>
            <el-menu-item index="4-2">cảm biến khoảng cách</el-menu-item> -->
            </el-menu-item>
            <el-menu-item index="5" @click="handleModel">
              <span>Quản lý mô hình</span>
            </el-menu-item>
            <el-menu-item index="6" @click="handleUser">
              <span>Quản lý user </span>
            </el-menu-item>
          </el-menu>
        </el-col>
        <el-col :span="18" style="margin: 8px 0px 0px 8px">
          <keep-alive>
            <router-view />
          </keep-alive>
        </el-col>

      </el-row>
    </el-main>


  </el-container>

</template>

<script>
  export default {
    data(){
      return {
        userLogin:{
          username: '',
          password: ''
        }
      }
    },
    methods: {
      handleUser() {
        if (!(this.$router.history.current.path === "/users"))
          this.$router.push('/users')
      },
      handleParking() {

        if (!(this.$router.history.current.path === "/parkings")) {
          this.$router.push('/parkings')
        }

      },
      handleDevice() {
        if (!(this.$router.history.current.path === "/devices"))
          this.$router.push('/devices')
      },
      handleVehicle() {
        if (!(this.$router.history.current.path === "/vehicles"))
          this.$router.push('/vehicles')
      },
      handleModel() {
        if (!(this.$router.history.current.path === "/models"))
          this.$router.push('/models')
      },
      handleLogout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        this.$router.push('/login');
      },
      handleEntrance() {
        if (!(this.$router.history.current.path === "/entrances"))
          this.$router.push('/entrances')
      },
      getLocalUser(){
        if(localStorage.getItem('user')!==null)
          this.userLogin= JSON.parse(localStorage.getItem('user'));
        
      }      
    },
    created(){
      this.getLocalUser();
    }
  }

</script>

<style>
  .el-header {
    top: 0;
    left: 0;
    padding: 0;
  }

  header.el-header {
    height: 50px !important;
  }
  .el-main{
    padding: 0;
  }

</style>
