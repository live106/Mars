namespace java com.live106.mars.protocol.thrift

include "../client/MarsProtocol.thrift"
include "../client/CLogin.thrift"
include "../client/CSystem.thrift"

struct RequestServerLogin
{
	1: MarsProtocol.PeerType type,
	2: i32 id
}

struct ResponseServerLogin
{
	1: bool	result,
	2: string msg
}

service IUserService 
{
	string ping(1: string visitor)
	
	CLogin.ResponseAuthServerPublicKey getPubKey(1: CLogin.RequestAuthServerPublicKey request, 2: string channelId) throws (1: CSystem.Notify notify)
	CLogin.ResponseSendClientPublicKey sendPubKey(1: CLogin.RequestSendClientPublicKey request, 2: string channelId) throws (1: CSystem.Notify notify)
	CLogin.ResponseUserLogin doLogin(1: CLogin.RequestUserLogin request, 2: string channelId) throws (1: CSystem.Notify notify)
	
}

service IMasterService
{
	//ResponseServerLogin serverRegister(1: RequestServerLogin request)
}
