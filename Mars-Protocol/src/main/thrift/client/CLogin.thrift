namespace java com.live106.mars.protocol.thrift
namespace csharp Net.Thrift

//*************************
//��¼��������ؿ�ʼ
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

//***DH��Կ������ʽ�����ڿͻ��˲�����ʵ�֣���ʱʹ�ü򻯰�ĵ�¼���̣��ͻ���ֱ�ӷ��ͱ���AES����key������������Կ��������ʡ�ԡ�

//�����ȡ��¼��������Կ
struct RequestAuthServerPublicKey
{
}
//��¼���������ع�Կ
struct ResponseAuthServerPublicKey
{
	1: string serverPubKey
}

//***DH������

//���Ϳͻ��˹�Կ
struct RequestSendClientPublicKey
{
	1: string clientPubKey//�ͻ������ɵļ���key����Ҫ��Base64����
}
//�˺ŷ�����ȷ�Ͽͻ��˹�Կ
struct ResponseSendClientPublicKey
{
	1: bool result,
	2: string msg//ʧ��ʱ�Ĵ�����Ϣ
}

struct RequestUserLogin
{
	1: string username,
	2: string password,
	3: string sdkUid,
	4: string sdkToken,
	5: string machineId,//���ڿͻ����޷���ȡ���ɷ�����α������
	6: required LoginType type = LoginType.USER_GUEST
	7: string sdk
	8: string sdkChannel
}

struct ResponseUserLogin
{
	1: required LoginCode code,
	2: required string msg,//��¼ʧ��ʱ�Ĵ�����Ϣ
	3: required i32 uid,//���uid
	4: string passport,//��Ӧ���uid��ͨ��֤
	5: string secureKey,//������Base64���������Ϸ��ͨѶʹ�õļ���key
	6: string gameserver
	7: string machineId//��һ�ε�¼ʱ���������·�α���machineId
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
	2: required i32 uid,//��Ӧ��¼�˺�ʱ���ص�ResponseUserLogin.uid
	3: i32	sequenceId,//�ͻ���ÿ������Ը�ֵ��������
	4: string randomKey//encrypt(gameserver+uid+sequeceId) by ResponseUserLogin.secureKey
	5: required string passport,//��Ӧ��¼�˺�ʱ���ص�ResponseUserLogin.passport
}

struct ResponseGameConnect
{
	1: required bool result,
	2: string msg,//��¼ʧ��ʱ�Ĵ�����Ϣ
	3: i32 playerid,
	4: string randomName
}
//*************************
//��¼��Ϸ����ؽ���
//*************************