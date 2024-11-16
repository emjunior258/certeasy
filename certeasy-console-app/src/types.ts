export interface Issuer {
    id: string
    name: string
    serial: string
    type: "ROOT" | "SUB_CA"
    dn: string
    path_length: number,
    parent?: {
        id: string
        name: string
    },
    children_count: number
}