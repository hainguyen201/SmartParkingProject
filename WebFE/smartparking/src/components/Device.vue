<template>
  <div style="margin-left: 16px">
    <div class="search">
      <el-row :gutter="20">
        <el-col :span="3">
          <el-button type="text">Tên thiết bị :</el-button>
        </el-col>
        <el-col :span="4">
          <el-input v-model="modelName"></el-input>
        </el-col>
        <el-col :span="3">
          <el-button type="text">Loại thiết bị:</el-button>
        </el-col>
        <el-col :span="4">
          <el-select v-model="modelType" placeholder="Select">
            <el-option
              v-for="item in ['sensor', 'servo']"
              :key="item.key"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row :gutter="4" type="flex" justify="start">
        <el-col :span="3">
          <el-button>Tìm kiếm</el-button>
        </el-col>
        <el-col :span="3">
          <el-button>Thiết lập lại</el-button>
        </el-col>
      </el-row>
    </div>
    <div class="table-info">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column fixed prop="id" label="ID" width="150">
        </el-table-column>
        <el-table-column prop="name" label="Tên thiết bị" width="120">
        </el-table-column>
        <el-table-column prop="type" label="Loại thiết bị" width="120">
        </el-table-column>
        <el-table-column prop="status" label="Trạng thái" width="120">
        </el-table-column>
        <el-table-column prop="name" label="Topic" width="120">
        </el-table-column>
        <el-table-column prop="name" label="Vị trí" width="120">
        </el-table-column>
        <el-table-column  width="120">
          <template slot-scope="scope">
            <el-button @click="handleClick" type="text" size="small"
              >Chi tiết</el-button
            >
            <el-button type="text" size="small">Sửa</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<style scoped>
.search {
  height: 10%vh;
}
.table-info {
  height: 90%vh;
}
.el-row {
  margin-bottom: 20px;
}
</style>
<script>
export default {
  name: "Device",
  data() {
    return {
      modelName: "",
      modelType: "",
      tableData: [
        {
          id: 1,
          name: "sonic-A12",
          type: "sonic sensor",
          status: "active"
        },{
          id: 1,
          name: "sonic-A12",
          type: "sonic sensor",
          status: "active"
        },{
          id: 1,
          name: "sonic-A12",
          type: "sonic sensor",
          status: "active"
        },{
          id: 1,
          name: "sonic-A12",
          type: "sonic sensor",
          status: "active"
        },{
          id: 1,
          name: "sonic-A12",
          type: "sonic sensor",
          status: "active"
        },
        
      ],
    };
  },
  methods:{
      handleClick() {
        console.log('click');
      }
  }
};
</script>
