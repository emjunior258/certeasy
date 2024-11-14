import { useMemo } from "react"
import useSWR from "swr"

const fetcher = async (url: string) => {
    url = `/api/${url.replace(/^\//, '')}`
    const res = await fetch(url)
    return res.json()
}

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

export interface UseIssuersParams {
    type?: '' | Issuer['type']
}

export const useIssuers = ({ type }: UseIssuersParams) => {
    const { data = [], ...props } = useSWR<Issuer[]>('/issuers', fetcher)
    const issuers = useMemo(() => {
        if (!type) return data
        return data.filter(item => item.type === type)
    }, [data, type])

    const counts = useMemo(() => {
        const root = data.filter(item => item.type === 'ROOT').length
        const subCA = data.filter(item => item.type === 'SUB_CA').length
        const total = data.length
        return { root, subCA, total }
    }, [data])

    return { issuers, counts, ...props }
}