import {restClient} from "./http.ts";

export type SignDocumentsCmd = {
    processId: number;
    personRole: string;
    code: string;
}

export type ResetSigningCodeCmd = {
    processId: number;
    personRole: string;
}

export class DocumentsSigningApi {
    constructor(private confirmUrl: string, private refreshUrl: string) {
    }

    signDocuments(cmd: SignDocumentsCmd): Promise<void> {
        return restClient.post(this.confirmUrl, cmd);
    }

    refreshSigningCode(cmd: ResetSigningCodeCmd): Promise<void> {
        return restClient.post(this.refreshUrl, cmd);
    }
}
