import background from "../public/background.jpeg";
import Image from "next/image";
import Layout from "../components/Layout";
import MeganPicture from "../components/AssetsAnimation/MegamanPicture";

export default function Home() {
  return (
    <>
      <Layout title="Home">
        <div className="absolute w-[3100px] h-full z-[-1]">
          <Image src={background} layout="fill" objectFit="fill" />
        </div>
        <MeganPicture />
      </Layout>
    </>
  );
}
