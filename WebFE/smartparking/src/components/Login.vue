<template>
  <div class="login">
    <el-card>
      <h3>Đăng nhập</h3>
      <el-form class="login-form" :model="model" :rules="rules" ref="form" @submit.native.prevent="login">
        <el-form-item prop="username">
          <el-input v-model="model.username" placeholder="Tài khoản" prefix-icon="fas fa-user"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="model.password" placeholder="Mật khẩu" type="password" prefix-icon="fas fa-lock">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" class="login-button" type="primary" native-type="submit('form')" block>Đăng nhập
          </el-button>
          <router-link :to="'register'">Đăng ký</router-link>
        </el-form-item>
        <!-- <a class="forgot-password" href="">Forgot password ?</a> -->
      </el-form>
    </el-card>
  </div>
</template>
<style scoped>
  .bg {
    height: 100vh;
    width: 100vw;
    background-image: 'assets/loginbg.jpg';
  }

  .login {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .login-button {
    width: 100%;
    margin-top: 40px;
  }

  .login-form {
    width: 290px;
  }

  .forgot-password {
    margin-top: 10px;
  }

</style>

<script>
  import {
    mapActions
  } from "Vuex";
  export default {
    name: "Login",
    data() {
      return {
        validCredentials: {
          username: "lightscope",
          password: "lightscope"
        },
        model: {
          username: "",
          password: ""
        },
        loading: false,
        rules: {
          username: [{
              required: true,
              message: "Username is required",
              trigger: "blur"
            },
            {
              min: 4,
              message: "Username length should be at least 5 characters",
              trigger: "blur"
            }
          ],
          password: [{
              required: true,
              message: "Password is required",
              trigger: "blur"
            },
            {
              min: 5,
              message: "Password length should be at least 5 characters",
              trigger: "blur"
            }
          ]
        }
      }
    },
    methods: {
      ...mapActions(['loginService']),
      simulateLogin() {
        return new Promise(resolve => {
          setTimeout(resolve, 800);
        });
      },
      async login() {
        this.$refs.form.validate((valid) => {
          if (valid) {
            this.loading = true;
            this.loginService(this.model).then(data => {
              this.loading = false;
              if (data === 'error')
                this.$message.error("Username or password is invalid");
              else {
                if (data.status === 200) {
                  localStorage.setItem('user', JSON.stringify(this.model))
                  localStorage.setItem('token', 'Bearer ' + data.data.token);
                  this.$router.push('/vehicles');
                }

              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });


        // if (
        //   this.model.username === this.validCredentials.username &&
        //   this.model.password === this.validCredentials.password
        // ) {
        //   this.$message.success("Login successfull");
        // } else {
        //   this.$message.error("Username or password is invalid");
        // }
      }
    }
  }

</script>
