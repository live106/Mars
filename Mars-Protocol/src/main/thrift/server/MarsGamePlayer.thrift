namespace java com.live106.mars.protocol.thrift.game

include "../client/CLogin.thrift"

struct MessagePlayerSecureInfo
{
	1: i32 uid,
	2: string passport,
	3: string secureKey,
	4: string channelId//�����ӿ�����Ҫʹ��
}

service IGamePlayerService 
{
	string ping(1: string visitor)
	
	bool setPlayerSecureKey(1: MessagePlayerSecureInfo secureInfo)
	
	CLogin.ResponseGameConnect clientLogin(1: CLogin.ReuqestGameConnect request)
}
