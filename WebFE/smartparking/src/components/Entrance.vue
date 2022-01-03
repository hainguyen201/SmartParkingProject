<template>
  <div class="entrance">
    <el-row class="selectcamera">
      <el-col :span="3"><span style="line-height: 40px">Chọn camera:</span></el-col>
      <el-col :span="8">
        <el-select v-model="value" placeholder="Select" filterable @change="updateCamera($event)">
        <el-option v-for="(item, index) in cameraIP" :key="index" :label="item.name" :value="item.address">
        </el-option>
      </el-select>
      </el-col>
    </el-row>
    <el-row class="view">
      <el-col>
        <img :src="value" ref="cameraVideo"  style="height:400px"/>
      </el-col>
      <!-- <img src="http:/192.168.1.3:4747/" alt=""> -->
    </el-row>
    <el-row class="gate"> </el-row>
  </div>
</template>
<style scoped>
  .el-row {
    margin-bottom: 4px;
  }

</style>
<script>
  import {
    mapActions
  } from "Vuex";
  import moment from "moment";
  export default {
    name: "Entrance",
    data() {
      return {
        value: null,
        cameraIP: [{
            name: "camera droid",
            address: "http://192.168.1.2:4747/video2",
          },
          {
            name: "camera test",
            address: "http://localhost:8787/video_feed",
          },
        ],
      };
    },

    methods: {
      ...mapActions(["captureImageService", 'getAllDeviceService']),
      updateCamera(e) {
        console.log(e);
      },
      captureImage() {
        const self = this;
        setInterval(function () {
        //   const ip = self.$refs.cameraVideo.src;
        const ip='http://192.168.1.2:4747/video'
          if (self.checkValidImageSrc(ip)) {
            const cameraIP = {
              camera_ip: ip,
            };
            self.captureImageService(cameraIP);
            console.log("video: ", ip);
          }
        }, 5000);
      },
      checkValidImageSrc(ip) {
        if (ip !== null && ip !== "http://localhost:8080/") return true;
        return false;
      },
    },
    mounted: function () {
      // this.captureImage();
    },
    created(){
      this.cameraIP=[];
      this.getAllDeviceService().then(data=>{
        data.forEach(element => {
          if(element.type===3){
            this.cameraIP.push(element)
          }
        });
        console.log(this.cameraIP);
      })
      
    }
  };

</script>
