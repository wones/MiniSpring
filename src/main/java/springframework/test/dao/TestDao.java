package springframework.test.dao;

import springframework.annotation.Repository;

@Repository
public class TestDao {
    public String echo(){
        return "this is TestDao";
    }
}
