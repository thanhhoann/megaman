import Image from 'next/image'
import megaman_screen_loader from "../../public/megaman_screen_loader.gif";

export default function MeganPicture() {
  return (
    <>
      <div className="absolute top-[310px] left-[500px]  border-8 border-[#260e0e] grid justify-center">
        <Image src={megaman_screen_loader} width={100} height={100} />
      </div>
    </>
  );
}
