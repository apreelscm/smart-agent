
class FetchRestClient implements RestClient {
    get<R>(url: string): Promise<R> {
        return this.request<never, R>('GET', url);
    }

    post<R, B>(url: string, data: B): Promise<R> {
        return this.request('POST', url, data);
    }

    private request<B, R>(method: string, url: string, data?: B): Promise<R> {
        return new Promise((resolve, reject) => {
            fetch(url, {
                method: method,
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json",
                },
                body: data ? JSON.stringify(data) : undefined,
            }).then((r) => {
                if (r.ok) {
                    resolve(r.text().then(t => t ? JSON.parse(t) : undefined))
                } else {
                    reject(r)
                }
            });
        });
    }
}

export interface RestClient {
    get<T>(url: string): Promise<T>;
    post<T, D>(url: string, data: D): Promise<T>;
}

export const restClient: RestClient = new FetchRestClient();
