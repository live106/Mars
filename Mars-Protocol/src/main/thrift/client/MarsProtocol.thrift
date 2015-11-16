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
	1: PeerType targetType = PeerType.PEER_TYPE_DEFAULT,//消息目标类型，对于客户端来说是不同的服务器，账号、游戏等
	2: PeerType sourceType = PeerType.PEER_TYPE_CLIENT,//消息发送者类型，客户端为PEER_TYPE_CLIENT
	3: i32 targetId,//消息目标身份id，保留字段
	4: i32 sourceId = -1,//消息发送者身份id，对于客户端为uid
	5: i64 channelId,//消息客户端channelId，服务器来维护该字段，客户端可忽略
	6: SerializeType serializeType = SerializeType.SERIALIZE_TYPE_THRIFT,//标识消息的序列化类型， 默认应该使用SERIALIZE_TYPE_THRIFT
	7: i32 protocolHash,//消息类名称的hashcode
	8: byte flag,//系统保留字段，方便扩展
	9: bool closeSocket = false//是否需要关闭socket
	10: string passport//通行证
}

/**
 * 协议格式如下：
 * TotalLength		HeaderLen		HeaderBytes		BodyLength		BodyBytes
 * 总长度				头长度				头序列化字节流		消息体长度			消息体序列化字节流
 */

/**
 * 字符串hashcode算法如下：
 	假设value变量为字符串
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