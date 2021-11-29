<template>
  <div style="font-size: 16px">
    <el-row class="searchParking" :gutter="10">
      <el-col :span="2" class="center"><span class="search-label">Tên bãi đỗ: </span> </el-col>
      <el-col :span="4">
        <el-input placeholder="Nhập tên bãi đỗ" v-model="parkingName"></el-input>
      </el-col>
      <el-col :span="2" class="center"><span class="search-label">Mã bãi đỗ: </span> </el-col>
      <el-col :span="4">
        <el-input placeholder="Nhập mã bãi đỗ" v-model="parkingCode"></el-input>
      </el-col>
      <el-col :span="2"><span class="search-label">Loại bãi đỗ: </span> </el-col>
      <el-col :span="4">
        <el-select v-model="parkingType" filterable placeholder="Chọn loại bãi đỗ">
          <el-option label="Xe máy" value="1"></el-option>
          <el-option label="Ô tô" value="2"></el-option>
          <el-option label="Tất cả" value="0"></el-option>
        </el-select>
      </el-col>
    </el-row>

    <el-row class="buttonSearch" justify="center" style="margin-top: 16px">
      <el-button type="primary" round>Tìm kiếm</el-button>
    </el-row>

    <el-row class="parkingItems" style="margin-top: 16px" v-if="this.parkingType==-1">
      <el-col :span="2" v-for='i in 8' :key="i" v-bind:class="{active: (i+1)%2, 'col-margin': (i+1)%2}">
        <el-card :body-style="{padding: '4px'}" v-for="n in 8" :key="n">
          <img src="../assets/car.png" alt="" v-if="n%2">
        </el-card>
      </el-col>
    </el-row>

    <el-table :data="tableFilter" style="width: 100%">
      <el-table-column fixed prop="parkingId" label="Mã bãi đỗ" width="150">
      </el-table-column>
      <el-table-column prop="name" label="Tên bãi đỗ" width="120">
      </el-table-column>
      <el-table-column prop="occupiedSlot" label="Chỗ đã đỗ" width="120">
      </el-table-column>
      <el-table-column prop="maxSlot" label="Số lượng tối đa" width="140">
      </el-table-column>
      <el-table-column prop="type" label="Loại bãi đỗ" width="300">
      </el-table-column>
      <el-table-column fixed="right" label="Thao tác" width="120">
        <template slot-scope="scope">
          <el-button @click="handle" type="text" size="small">Detail</el-button>
          <el-button type="text" size="small">Edit</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-row class="pagination">
      <el-pagination layout="prev, pager, next" :total="10" :page-size="1">
      </el-pagination>
    </el-row>

  </div>
</template>
<style scoped>
  .search-label {
    line-height: 40px;
  }

  .el-card {
    height: 40px;
    margin: 4px;
    border: 1px solid rgb(15, 114, 226);
    border-radius: 10px;
  }

  img {
    object-fit: cover;
    width: 75px;
    height: 30px;
  }

  .col-margin {
    margin-right: 64px;
  }

</style>
<script>
  export default {
    name: "Parking",
    data() {
      return {
        parkingType: '',
        parkingName: '',
        parkingCode: '',
        parkingArea: [
          [{
              status: 1,
              id: "A12"
            },
            {
              status: 1,
              id: "A12"
            },
            {
              status: 1,
              id: "A12"
            },
          ]
        ],
        tableData:[
            {
                parkingId: 1,
                occupiedSlot: 23,
                name: "Tầng 1",
                maxSlot: 30,
                type: 1
            },
            {
                parkingId: 1,
                occupiedSlot: 23,
                name: "Tầng 1",
                maxSlot: 30,
                type: 1
            },
            
        ]
      }
    },
    methods:{
        handleDetail(){

        }
    },
    computed:{
        tableFilter(){
            if(this.tableData){
                this.tableData.forEach(element => {
                    if(element.type===1){
                        element.type='xe máy'
                    }else{
                        element.type='ô tô'
                    }
                });
            }
        }
    }
    
  };

</script>
