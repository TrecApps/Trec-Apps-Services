export class Client {
    name: String;
    clientId: String;
    clientSecret: String;
}

export class ClientError {
    client: Client;
    error: String;
}