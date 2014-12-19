package dsn.scheduler;

import java.io.IOException;
import java.sql.SQLException;

import dsn.commons.interop.service.InteropTransferObject;
import dsn.commons.interop.service.provider.*;
import dsn.scheduler.DBInfo;
import dsn.scheduler.DSNJobHelper;

public class SchedulerInteropHandler extends InteropHandler {

	public static DBInfo dbinfo;
	public static DSNJobHelper dsnjobhelper;


	public InteropTransferObject RegisterAccessToken(InteropTransferObject params) {
		dbinfo = new DBInfo();

		String user_id = params.getValue("user_id");
		String sp_id = params.getValue("sp_id");
		String sp_login_id = params.getValue("sp_login_id");
		String token = params.getValue("token");
		String secret = params.getValue("secret");

		//dbinfo.insertAccessToken(user_id, sp_id, sp_login_id, token, secret);
		dbinfo.checkUserAccountExists(user_id, sp_id, sp_login_id, token, secret);
		
		return new InteropTransferObject();
	}

	public InteropTransferObject GetLatestStatuses(InteropTransferObject params) throws Exception {
		dbinfo = new DBInfo();
		dsnjobhelper = new DSNJobHelper();

		String user_id = params.getValue("user_id");

		dsnjobhelper.passDataThisUser(user_id);

		return this.GetStatuses(params);
	}

	public InteropTransferObject GetStatuses(InteropTransferObject params) throws Exception {
		dbinfo = new DBInfo();

		String user_id = params.getValue("user_id");
		InteropTransferObject to = new InteropTransferObject();

		String statusesThisUser[][] = dbinfo.getStatusesThisUser(user_id);
		
		//for(int i = 0; i < statusesThisUser.length; i++) {
			to.add("sp_id", statusesThisUser[1]);
			to.add("status", statusesThisUser[2]);
			to.add("timestamp", statusesThisUser[3]);	
			to.add("sm_sp_id", statusesThisUser[4]);
		//}
		
		return to;
	}
}