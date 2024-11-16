import { NavigateOptions, useSearchParams } from "react-router-dom";

export function useQueryParams<T extends object>(defaultInit?: T) {
    const [params, setParams] = useSearchParams(defaultInit as Record<string, string>)
    const setQueryParams = (value: T, options: NavigateOptions) => setParams({ ...Object.fromEntries(params), ...value }, options)
    return [Object.fromEntries(params) as T, setQueryParams] as const
}