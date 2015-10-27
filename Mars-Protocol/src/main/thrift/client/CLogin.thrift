namespace java com.live106.mars.protocol.thrift
namespace csharp Net.Thrift

//*************************
//��¼��������ؿ�ʼ
//*************************

enum LoginCode
{
	OK = 1,
	ERROR = 2
}

//�����ȡ��¼��������Կ
struct RequestAuthServerPublicKey
{
}
//��¼���������ع�Կ
struct ResponseAuthServerPublicKey
{
	1: string serverPubKey
}
//���Ϳͻ��˹�Կ
struct RequestSendClientPublicKey
{
	1: string clientPubKey
}
//��¼������ȷ�Ͽͻ��˹�Կ
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
//��¼��������ؽ���
//*************************

//*************************
//��¼��Ϸ����ؿ�ʼ
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
//��¼��Ϸ����ؽ���
//*************************