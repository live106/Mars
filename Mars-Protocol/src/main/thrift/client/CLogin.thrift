namespace java com.live106.mars.protocol.thrift
namespace csharp Net.Thrift

//*************************
//登录服务器相关开始
//*************************

enum LoginCode
{
	OK = 1,
	ERROR = 2
}

//请求获取登录服务器公钥
struct RequestAuthServerPublicKey
{
}
//登录服务器返回公钥
struct ResponseAuthServerPublicKey
{
	1: string serverPubKey
}
//发送客户端公钥
struct RequestSendClientPublicKey
{
	1: string clientPubKey
}
//登录服务器确认客户端公钥
struct ResponseSendClientPublicKey
{
	1: bool result,
	2: string msg
}

struct RequestUserLogin
{
	1: string username,
	2: string password,
	3: string sdkUid,
	4: string sdkToken,
	5: string machineId
}

struct ResponseUserLogin
{
	1: required LoginCode code,
	2: required string msg,
	3: required i32 uid,
	4: required string passport,
	5: required string secureKey,
	6: string gameserver
}

//*************************
//登录服务器相关结束
//*************************

//*************************
//登录游戏器相关开始
//*************************
struct ReuqestGameConnect
{
	1: string gameserver,
	2: required i32 uid,
	3: i32	sequenceId,
	4: string randomKey//encrypt(gameserver+uid+sequeceId) by ResponseUserLogin.secureKey
	5: required string passport,
}

struct ResponseGameConnect
{
	1: required bool result,
	2: string msg,
	3: i32 playerid,
	4: string randomName
}
//*************************
//登录游戏器相关结束
//*************************