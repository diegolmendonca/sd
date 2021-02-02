import React from 'react';
import { Image } from 'antd';


const MyList = (props) => {
    const { images } = props;
    if (!images || images.length === 0) return <p>No images, sorry</p>;
    return (
        <ul>
            <h2 className='list-head'>Available Images</h2>
            {images.map((im) => {
                var concat = 'data:image/jpeg;base64,' + im.image;
                return (
                    <div class="container">

                        <Image.PreviewGroup>
                            <Image
                                width={200}
                                src={concat}
                            />
                        </Image.PreviewGroup>
                    </div>
                );
            })}
        </ul>
    );
};
export default MyList;