<template>
  <div class="register">
    <el-card>
      <h3>Đăng ký</h3>
      <el-form class="register-form" :model="user" :rules="rules" ref="form" @submit.native.prevent="register">
        <el-form-item prop="username">
          <el-input v-model="user.username" placeholder="Tài khoản" prefix-icon="fas fa-user"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="user.password" placeholder="Mật khẩu" type="password" prefix-icon="fas fa-lock">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" class="register-button" type="primary" native-type="submit" block>Đăng ký
          </el-button>
          <div> Đã có tài khoản?
            <router-link :to="'login'">Đăng nhập</router-link>
          </div>
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

  .register {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .register-button {
    width: 100%;
    margin-top: 40px;
  }

  .register-form {
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
  export default ({
    name: "Register",
    data() {
      return {
        user: {
          username: null,
          password: null,
        },
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
        },
        loading: false,
      }
    },
    methods: {
      ...mapActions(['registerService']),
      register(formName) {
        this.$refs.form.validate((valid) => {
          if (valid) {
            this.registerService(this.user).then(data => {
              debugger
              if (data != null && typeof(data)!='string')
                this.$message({
                  message: 'Bạn đã đăng ký thành công, chờ phê duyệt',
                  type: 'success'
                });
              else if(typeof(data)=='string'){
                this.$message.error(data);
              }else
                this.$message.error('Lỗi khi đăng ký');
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });


      }
    }
  })

</script>
