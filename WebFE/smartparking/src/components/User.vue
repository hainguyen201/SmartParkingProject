<template>
  <div>
    <el-button @click="getAllUser">Getuser</el-button>
    <input type="file" accept="image/jpeg" ref="avatarImage" />
    <el-button @click="addUser">PostUser</el-button>
  </div>
</template>
<script>
import { mapActions, mapGetters } from "Vuex";
export default {
  name: "User",
  data() {
    return {
      avatar: null,
    };
  },
  methods: {
    ...mapActions(["addUserService"]),
    previewImage(e) {
      console.log("");
    },
    getAllUser() {
      console.log("");
    },
    addUser() {
      let image = this.$refs.avatarImage.files[0];
      const reader = new FileReader();
      let fd= new FormData()
      if (typeof image !== "undefined") {
        reader.readAsDataURL(image);
        reader.onload = (e) => {
          this.avatar = e.target.result.split(",")[1];
          console.log(this.avatar);
          let user = {
            name: "hainvis",
          };
          this.addUserService(user);
        };
      }
    },
  },
};
</script>
