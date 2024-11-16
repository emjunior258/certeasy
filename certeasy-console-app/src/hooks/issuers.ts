import { useMemo } from "react"
import useSWR from "swr"
import type { Issuer as IssuerType } from "@/types"

const fetcher = async (url: string) => {
    url = `/api/${url.replace(/^\//, '')}`
    const res = await fetch(url)
    return res.json()
}

export type Issuer = IssuerType

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