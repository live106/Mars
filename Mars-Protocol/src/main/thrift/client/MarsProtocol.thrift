namespace java com.live106.mars.protocol.thrift
namespace csharp Net.Thrift

enum PeerType
{
	PEER_TYPE_MASTER = 1,
	PEER_TYPE_ACCOUNT = 5,
	PEER_TYPE_GAME = 10,
	PEER_TYPE_CLIENT = 15,
	PEER_TYPE_DEFAULT = 20
}

enum SerializeType
{
	SERIALIZE_TYPE_THRIFT = 1,
	SERIALIZE_TYPE_PROTOBUFFER = 2
}

struct ProtocolHeader
{
	1: PeerType targetType = PeerType.PEER_TYPE_DEFAULT,//��ϢĿ�����ͣ����ڿͻ�����˵�ǲ�ͬ�ķ��������˺š���Ϸ��
	2: PeerType sourceType = PeerType.PEER_TYPE_CLIENT,//��Ϣ���������ͣ��ͻ���ΪPEER_TYPE_CLIENT
	3: i32 targetId,//��ϢĿ�����id�������ֶ�
	4: i32 sourceId = -1,//��Ϣ���������id�����ڿͻ���Ϊuid
	5: i64 channelId,//��Ϣ�ͻ���channelId����������ά�����ֶΣ��ͻ��˿ɺ���
	6: SerializeType serializeType = SerializeType.SERIALIZE_TYPE_THRIFT,//��ʶ��Ϣ�����л����ͣ� Ĭ��Ӧ��ʹ��SERIALIZE_TYPE_THRIFT
	7: i32 protocolHash,//��Ϣ�����Ƶ�hashcode
	8: byte flag,//ϵͳ�����ֶΣ�������չ
	9: bool closeSocket = false//�Ƿ���Ҫ�ر�socket
	10: string passport//ͨ��֤
}

/**
 * Э���ʽ���£�
 * TotalLength		HeaderLen		HeaderBytes		BodyLength		BodyBytes
 * �ܳ���				ͷ����				ͷ���л��ֽ���		��Ϣ�峤��			��Ϣ�����л��ֽ���
 */

/**
 * �ַ���hashcode�㷨���£�
 	����value����Ϊ�ַ���
	public int hashCode() {
	    int h = hash;
	    if (h == 0 && value.length > 0) {
	        char val[] = value;
	
	        for (int i = 0; i < value.length; i++) {
	            h = 31 * h + val[i];
	        }
	        hash = h;
	    }
	    return h;
	}
 */