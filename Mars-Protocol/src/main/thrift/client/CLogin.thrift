namespace java com.live106.mars.protocol.thrift
namespace csharp Net.Thrift

//*************************
//登录服务器相关开始
//*************************

enum LoginType
{
	USER_GUEST = 1,
	USER_ACCOUNT = 2,
	USER_THIRDPARTY = 3
}

enum LoginCode
{
	OK = 1,
	ERROR = 2
}

//***DH秘钥交换方式，由于客户端不方便实现，暂时使用简化版的登录流程，客户端直接发送本地AES加密key给服务器，公钥交换过程省略。

//请求获取登录服务器公钥
struct RequestAuthServerPublicKey
{
}
//登录服务器返回公钥
struct ResponseAuthServerPublicKey
{
	1: string serverPubKey
}

//***DH结束。

//发送客户端公钥
struct RequestSendClientPublicKey
{
	1: string clientPubKey//客户端生成的加密key，需要做Base64编码
}
//账号服务器确认客户端公钥
struct ResponseSendClientPublicKey
{
	1: bool result,
	2: string msg//失败时的错误信息
}

struct RequestUserLogin
{
	1: string username,
	2: string password,
	3: string sdkUid,
	4: string sdkToken,
	5: string machineId,//由于客户端无法获取，由服务器伪造生成
	6: required LoginType type = LoginType.USER_GUEST
	7: string sdk
	8: string sdkChannel
}

struct ResponseUserLogin
{
	1: required LoginCode code,
	2: required string msg,//登录失败时的错误信息
	3: required i32 uid,//玩家uid
	4: string passport,//对应玩家uid的通行证
	5: string secureKey,//进行了Base64编码的与游戏服通讯使用的加密key
	6: string gameserver
	7: string machineId//第一次登录时，服务器下发伪造的machineId
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
	2: required i32 uid,//对应登录账号时返回的ResponseUserLogin.uid
	3: i32	sequenceId,//客户端每次请求对该值自增处理
	4: string randomKey//encrypt(gameserver+uid+sequeceId) by ResponseUserLogin.secureKey
	5: required string passport,//对应登录账号时返回的ResponseUserLogin.passport
}

struct ResponseGameConnect
{
	1: required bool result,
	2: string msg,//登录失败时的错误信息
	3: i32 playerid,
	4: string randomName
}
//*************************
//登录游戏器相关结束
//*************************