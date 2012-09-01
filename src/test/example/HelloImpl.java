package test.example;

public class HelloImpl implements Hello {

	@Override
	public String hello(String name) {
		//throw new RuntimeException("Have Error!");
		System.out.println("Hello, "+name+"!");
		
		return "Hello1, "+name+"!";
	}

	@Override
	public User getUser(int id) {
		{
			User u=new User();
			u.setAge(1);
			u.setEmail("zhuzhsh@163.com");
			u.setId(1);
			u.setName("zhuzhsh");
			for(int i=0;i<5;i++){
				Role r=new Role();
				r.setName("role"+i);
				r.setDescription("role"+i);
				u.addRole(r);
			}
			return u;
		}
	}

}
