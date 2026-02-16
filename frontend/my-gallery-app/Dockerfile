# syntax=docker/dockerfile:1.19
FROM oven/bun:1.2.5-alpine AS build
WORKDIR /build

COPY . .

RUN --mount=type=cache,target=/root/.bun \
    bun install

RUN bun run build

FROM nginx:1.29.3-alpine3.22

COPY deploy/ /etc/nginx/conf.d/

COPY --from=build /build/dist /usr/share/nginx/statics/image-gallery
