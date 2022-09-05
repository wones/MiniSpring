package springframework.test.service;

import springframework.annotation.Autowired;
import springframework.annotation.Service;
import springframework.test.dao.TestDao;

@Service
public class TestService {
    @Autowired
    TestDao testDao;

    public void echo(){
        System.out.println(testDao.echo());
    }
}
