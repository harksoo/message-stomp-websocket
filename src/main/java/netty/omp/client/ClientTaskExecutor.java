package netty.omp.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import com.telcoware.omp.OccmRecvQueue;

public class ClientTaskExecutor {

	static final Logger logger = Logger.getLogger(LoginClientHandler.class);
	
	//@Autowired
	OccmRecvQueue queue;
	
	private LoginClient clientLogin; 
	private RouteClient clientRoute;
	
	public void clientLoginSendMessage(String msg){
		// TODO client의 lifecycle 확인 요망.
		if(clientLogin != null)
			clientLogin.sendMessage(msg);
	}
	
	public void clientRouteSendMessage(String msg){
		// TODO client의 lifecycle 확인 요망.
		if(clientRoute != null)
			clientRoute.sendMessage(msg);
	}
	
	private TaskExecutor taskExecutor;

    public ClientTaskExecutor(TaskExecutor taskExecutor, OccmRecvQueue queue) {
        this.taskExecutor = taskExecutor;
        this.queue = queue;
    }

    public void runClient() {

    	// TODO 순차적인 실행 관계를 만들어야 하나 우선은 테스트 용으로 처리함.    	
    	clientLogin = new LoginClient(queue);
    	taskExecutor.execute(clientLogin);
    	
    	clientLogin.Login();
    	
    	logger.debug("Login process complete =============================== ");
    	
    	clientRoute = new RouteClient(queue);
    	taskExecutor.execute(clientRoute);

    }

}
