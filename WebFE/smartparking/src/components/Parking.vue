<template>
  <div style="font-size: 16px">
    <el-row class="searchParking" :gutter="10">
      <el-col :span="3" class="center"><span class="search-label">Tên bãi đỗ: </span>
      </el-col>
      <el-col :span="4">
        <el-input placeholder="Nhập tên bãi đỗ" v-model="parkingName"></el-input>
      </el-col>
      <el-col :span="3" class="center"><span class="search-label">Mã bãi đỗ: </span>
      </el-col>
      <el-col :span="4">
        <el-input placeholder="Nhập mã bãi đỗ" v-model="parkingCode"></el-input>
      </el-col>
      <!-- <el-col :span="3"><span class="search-label">Loại bãi đỗ: </span>
      </el-col>
      <el-col :span="4">
        <el-select v-model="parkingType" filterable placeholder="Chọn loại bãi đỗ" autocomplete>
          <el-option label="Xe máy" value="1" ></el-option>
          <el-option label="Ô tô" value="2"></el-option>
        </el-select>
      </el-col> -->
    </el-row>

    <el-row class="buttonSearch" justify="center" style="margin-top: 16px">
      <el-button type="primary" plain round>
        <i class="el-icon-search"></i> Tìm kiếm</el-button>
    </el-row>

   
      <el-dialog :visible.sync="parkingAreaDetail"  width="80%">
        <el-carousel height="400px" :autoplay="false">
        <el-carousel-item v-for="item in 4" :key="item">
          <el-row>
            <el-col :span="2" v-for="i in 8" :key="i" v-bind:class="{ active: true, 'col-margin': (i + 1) % 2, 'first-margin': i===1 }">
            
            <el-card :body-style="{ padding: '4px' }" v-for="n in 8" :key="n" style="text-align: center">
              <el-tooltip class="item" effect="dark" :content="'A'+item+i+n" placement="top">
                <img src="../assets/car.png" alt="" v-if="n % 2" />
                <el-button v-if="(n%2)!==1" style="border:none" type="text">A{{item}}{{i}}{{n}}</el-button>
              </el-tooltip>
            </el-card>
          </el-col>
          </el-row>
          
        </el-carousel-item>
      </el-carousel>
        <span slot="footer" class="dialog-footer">
          <el-button @click="parkingAreaDetail = false">Đóng</el-button>
        </span>
      </el-dialog>
    

    <el-table :data="listParkingArea" style="width: 100%">
      <el-table-column fixed prop="id" label="Mã bãi đỗ" width="150">
      </el-table-column>
      <el-table-column prop="name" label="Tên bãi đỗ" width="120">
      </el-table-column>
      <el-table-column prop="numInUsed" label="Chỗ đã đỗ" width="120">
      </el-table-column>
      <el-table-column prop="maxNumber" label="Số lượng tối đa" width="140">
      </el-table-column>
      <!-- <el-table-column prop="type" label="Loại bãi đỗ" width="300">
      </el-table-column> -->
      <el-table-column label="Thao tác" width="120">
        <template slot-scope="scope">
          <el-button @click="showParkingDetail" type="text" size="small">Chi tiết</el-button>
          <el-button type="text" size="small">Sửa</el-button>
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
  .first-margin{
    margin-left: 64px;
  }

  .el-carousel__item:nth-child(n) {
    background-color: #9bacc4;
    border-radius: 5px;
  }

</style>
<script>
  export default {
    name: "Parking",
    data() {
      return {
        parkingType: null,
        parkingName: "",
        parkingCode: "",
        parkingAreaDetail: false,
        parkingArea: [
          [{
              status: 1,
              id: "A12",
            },
            {
              status: 1,
              id: "A12",
            },
            {
              status: 1,
              id: "A12",
            },
          ],
        ],
        listParkingArea: [{
          id: 1,
          name: "Bãi tầng 1A",
          numInUsed: 13,
          maxNumber: 50
        }],
      };
    },
    methods: {
      handleDetail() {},
      showParkingDetail() {
        this.parkingAreaDetail=true
      }
    },
    computed: {
      tableFilter() {
        if (this.tableData) {
          this.tableData.forEach((element) => {
            if (element.type === 1) {
              element.type = "xe máy";
            } else {
              element.type = "ô tô";
            }
          });
        }
      },
    },
  };

</script>
