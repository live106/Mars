namespace java com.live106.mars.protocol.thrift.game

include "../client/CLogin.thrift"
include "../client/CStore.thrift"
include "../client/CArchive.thrift"
include "../client/MarsProtocol.thrift"
include "../client/CSystem.thrift"
include "../client/CGold.thrift"

struct MessageUserSecureInfo
{
	1: i32 uid,
	2: string passport,
	3: string secureKey,
	4: string channelId//长连接可能需要使用
}

service IGamePlayerService 
{
	string ping(1: string visitor)
	
	//for client request transfer by Master	
	//CLogin.ResponseGameConnect clientLogin(1: CLogin.ReuqestGameConnect request)
	map<CLogin.ResponseGameConnect, bool> clientLogin(1: CLogin.ReuqestGameConnect request) throws (1: CSystem.Notify notify)
	map<CGold.ResponseSyncGold, bool> syncGold(1: CGold.RequestSyncGold request, 2: MarsProtocol.ProtocolHeader header) throws  (1: CSystem.Notify notify)
	
	//for server module call
	bool pay(1: i64 userId, 2: i32 amount, 3: string orderId, 4: string plainData) throws (1: CSystem.Notify notify)
	bool setPlayerSecureKey(1: MessageUserSecureInfo secureInfo)
}

service IGameStoreService
{
	map<CStore.ResponseStoreScenario, bool> storeScenarioData(1: CStore.RequestStoreScenario request, 2: MarsProtocol.ProtocolHeader header)
	map<CStore.ScenarioInfo, bool> loadScenarioData(1: CStore.RequestLoadScenario request, 2: MarsProtocol.ProtocolHeader header)
	
	map<CArchive.ResponseServerArchiveTime, bool> loadArchiveTimestamp(1: CArchive.RequestServerArchiveTime request, 2: MarsProtocol.ProtocolHeader header) throws (1: CSystem.Notify notify)
	map<CArchive.ResponseSaveArchive, bool> saveArchive(1: CArchive.RequestSaveArchive request, 2: MarsProtocol.ProtocolHeader header) throws (1: CSystem.Notify notify)
	map<CArchive.ResponseLoadArchive, bool> loadArchive(1: CArchive.RequestLoadArchive request, 2: MarsProtocol.ProtocolHeader header) throws (1: CSystem.Notify notify)
}
